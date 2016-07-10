package com.brown.luke.overwatchchooseahero.OWRecommend;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Recommender {
    // Constants
    //----------

    private static final short REALLY_REALLY_BIG = 1000;
    private static final short REALLY_BIG = 750;
    private static final short BIG = 500;

    // Fields
    //-------

    private static ArrayList<Hero> heroes;
    private static ArrayList<Stage> stages;
    private static SimpleDirectedWeightedGraph<Hero, WeightedEdge> counters;
    private static SimpleWeightedGraph<Hero, WeightedEdge> synergy;

    // Static block
    //-------------

    static  {
        heroes = HeroDB.getHeroes();
        counters = HeroDB.getCounters();
        synergy = HeroDB.getSynergy();
        stages = HeroDB.getStages();
    }


    // Main method
    //-------------

    public static ArrayList<String> run(final ArrayList<String> allyTeamStrings, final ArrayList<String> enemyTeamStrings, final String stageString, final String subMap) {
        ArrayList<Hero> allyTeam = new ArrayList<>();
        ArrayList<Hero> enemyTeam = new ArrayList<>();
        for(String allyName : allyTeamStrings) {
            allyTeam.add(HeroDB.getNameMap().get(allyName));
        }
        for(String enemyName : enemyTeamStrings) {
            enemyTeam.add(HeroDB.getNameMap().get(enemyName));
        }

        final State state;
        switch (subMap) {
            case "Attack":
                state = State.ATTACK;
                break;

            case "Defend":
                state = State.DEFEND;
                break;

            default:
                state = State.KOH;
                break;
        }

        final Stage stage = HeroDB.getStageNameMap().get(stageString);

        final ArrayList<Hero> rankedHeroes = new ArrayList<>(heroes);

        final HashMap<Role, Integer> roleCounts = new HashMap<>();
        roleCounts.put(Role.OFFENCE, 0);
        roleCounts.put(Role.DEFENCE, 0);
        roleCounts.put(Role.TANK, 0);
        roleCounts.put(Role.SUPPORT, 0);
        final HashMap<SubRole, Integer> subRoleCounts = new HashMap<>();
        subRoleCounts.put(SubRole.ASSAULT, 0);
        subRoleCounts.put(SubRole.FLANKER, 0);
        subRoleCounts.put(SubRole.SNIPER, 0);
        subRoleCounts.put(SubRole.BUILDER, 0);
        final HashSet<Hero> coverage = new HashSet<>();
        for(Hero ally : allyTeam) {
            // Calculate the amount of each role we got
            if(ally.getCore()) {
                roleCounts.put(ally.getRole(), roleCounts.get(ally.getRole()) + 1);
                if (ally.hasSubRole()) {
                    subRoleCounts.put(ally.getSubRole(), roleCounts.get(ally.getRole()) + 1);
                }
            }
            // Calculate our team's coverage
            Set<WeightedEdge> edges = counters.edgesOf(ally);
            for(WeightedEdge e : edges) {
                coverage.add(e.target());

            }
        }

        for(Hero hero : rankedHeroes) {
            // Make sure you got a support
            if(hero.getCore() && hero.getRole() == Role.SUPPORT && roleCounts.get(Role.SUPPORT) == 0) {
                hero.adjustRank(REALLY_REALLY_BIG);
            }

            // Make sure we got a tank
            if(hero.getCore() && hero.getRole() == Role.TANK && roleCounts.get(Role.TANK) == 0) {
                hero.adjustRank(REALLY_BIG);
            }

            // Don't recommend a third support
            if(hero.getCore() && hero.getRole() == Role.SUPPORT && roleCounts.get(Role.SUPPORT) >= 2) {
                hero.adjustRank(-1 * BIG);
            }

            if(state == State.ATTACK) {
                // Make sure we got 2 attack guys on attack
                if(hero.getCore() && hero.getRole() == Role.OFFENCE && roleCounts.get(Role.OFFENCE) < 2) {
                    hero.adjustRank(10);
                }

                // If we don't got an assault, boost assault
                if(hero.getSubRole() == SubRole.ASSAULT && subRoleCounts.get(SubRole.ASSAULT) == 0) {
                    hero.adjustRank(6);
                }

                // If we don't got a flanker, boost flanker
                if(hero.getSubRole() == SubRole.ASSAULT && subRoleCounts.get(SubRole.FLANKER) == 0) {
                    hero.adjustRank(6);
                }

                // Deboost more than 1 builders
                if(hero.getSubRole() == SubRole.BUILDER && subRoleCounts.get(SubRole.BUILDER) > 0) {
                    hero.adjustRank(-10);
                }
            } else if(state == State.DEFEND) {
                // Make sure we got 1 defence unit on defence
                if(hero.getRole() == Role.DEFENCE && roleCounts.get(Role.DEFENCE) == 0) {
                    hero.adjustRank(10);
                }

                // Boost a sniper if we don't got one on defence
                if(hero.getSubRole() == SubRole.SNIPER && subRoleCounts.get(SubRole.SNIPER) == 0) {
                    hero.adjustRank(6);
                }

                // Deboost more than 2 builders
                if(hero.getSubRole() == SubRole.BUILDER && subRoleCounts.get(SubRole.BUILDER) > 1) {
                    hero.adjustRank(-10);
                }
            } else if(state == State.KOH) {
                // Make sure we got at least 1 attacking unit on koh
                if(hero.getRole() == Role.OFFENCE && roleCounts.get(Role.OFFENCE) == 0) {
                    hero.adjustRank(BIG);
                }
            }

            // Deboost double sniper
            if(hero.getSubRole() == SubRole.SNIPER && subRoleCounts.get(SubRole.SNIPER) > 0) {
                hero.adjustRank(-10);
            }

            // Deboot triple tank
            if(hero.getCore() && hero.getRole() == Role.TANK && roleCounts.get(Role.TANK) >= 2) {
                hero.adjustRank(-10);
            }

            // Stage adjustment
            hero.adjustRank(((enemyTeam.size() == 0) ? 1.33f : 1) * stage.getRank(subMap, hero));

            for(Hero ally : allyTeam) {
                // Deboost same hero
                if(hero == ally) {
                    hero.adjustRank(-15);
                }
                if(synergy.containsEdge(hero, ally)) {
                    hero.adjustRank(synergy.getEdge(hero, ally).weight()); // Recommend hero's that synergize with team
                }
                if(enemyTeam.size() == 0) {
                    Set<WeightedEdge> edges = counters.edgesOf(hero);
                    for(WeightedEdge e : edges) {
                        final Hero t = e.target();
                        if(counters.containsEdge(t, ally)) {
                            hero.adjustRank(2); // Recommend hero's that counter our team's counters
                        }
                        if(!coverage.contains(t)) {
                            hero.adjustRank(2); // Recommend hero's that extend our coverage
                        }
                    }
                }
            }
            for(Hero enemy : enemyTeam) {
                if(counters.containsEdge(hero, enemy)) {
                    if(!coverage.contains(enemy)) {
                        hero.adjustRank(12); // If we counter an enemy that our team does not counter then boost!
                    }
                    hero.adjustRank(counters.getEdge(hero, enemy).weight()); // recommend heroes that counter the enemy
                }
                if(counters.containsEdge(enemy, hero)) {
                    hero.adjustRank(-1 * counters.getEdge(enemy, hero).weight()); // don't recommend heroes that enmy team counter
                }
            }
        }

        // Sort by new rank: Note maintains order for equal elements (i.e tier list)
        Collections.sort(rankedHeroes, new Comparator<Hero>() {
            @Override
            public int compare(Hero hero1, Hero hero2)
            {
                if(hero1.getRank() > hero2.getRank()) {
                    return -1;
                } else if (hero1.getRank() < hero2.getRank()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        final ArrayList<String> result = new ArrayList<>();
        for(Hero hero : rankedHeroes) {
            hero.resetRank();
            result.add(hero.getName());
        }

        return result;
    }
}
