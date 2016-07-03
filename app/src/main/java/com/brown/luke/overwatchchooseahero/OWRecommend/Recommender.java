package com.brown.luke.overwatchchooseahero.OWRecommend;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Recommender {
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

        final HashMap<Role, Integer> recommendRoleCount = new HashMap<>();
        recommendRoleCount.put(Role.OFFENCE, (state == State.ATTACK) ? 2 : ((state == State.KOH) ? 1 : 0));
        recommendRoleCount.put(Role.DEFENCE, (state == State.ATTACK) ? 0 : ((state == State.KOH) ? 1 : 2));
        recommendRoleCount.put(Role.TANK, 1);
        recommendRoleCount.put(Role.SUPPORT, 1);

        final HashMap<SubRole, Integer> recommendSubRoleCount = new HashMap<>();
        recommendSubRoleCount.put(SubRole.ASSAULT, (state == State.ATTACK) ? 1 : ((state == State.KOH) ? 1 : 0));
        recommendSubRoleCount.put(SubRole.FLANKER, (state == State.ATTACK) ? 1 : ((state == State.KOH) ? 1 : 0));
        recommendSubRoleCount.put(SubRole.SNIPER, (state == State.DEFEND) ? 1 : 0);
        recommendSubRoleCount.put(SubRole.BUILDER, 0);

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
            // If we are under the recommended roles then recommend hero's of that role
            if(hero.getCore() && roleCounts.get(hero.getRole()) < recommendRoleCount.get(hero.getRole())) {
                if(hero.getRole() == Role.SUPPORT) {
                    hero.adjustRank(5000); // Some stupid big number to make them play support even over tank
                } else if (hero.getRole() == Role.TANK) {
                    hero.adjustRank(1000); // Some stupid big number to make them play tank
                } else {
                    hero.adjustRank(7);
                }
            }
            if(hero.hasSubRole() && subRoleCounts.get(hero.getSubRole()) < recommendSubRoleCount.get(hero.getSubRole())) {
                hero.adjustRank(4);
            }

            // If we have too many of one role then don't recommend heroes of that role
            if(hero.getCore() && roleCounts.get(hero.getRole()) > recommendRoleCount.get(hero.getRole())) {
                hero.adjustRank(5 * (recommendRoleCount.get(hero.getRole()) - roleCounts.get(hero.getRole())));
            }
            if(hero.hasSubRole() && subRoleCounts.get(hero.getSubRole()) > recommendSubRoleCount.get(hero.getSubRole())) {
                hero.adjustRank(3 * (recommendSubRoleCount.get(hero.getSubRole()) - subRoleCounts.get(hero.getSubRole())));
            }

            hero.adjustRank(((enemyTeam.size() == 0) ? 1.5f : 1) * stage.getRank(subMap, hero));

            for(Hero ally : allyTeam) {
                if(hero == ally) {
                    hero.adjustRank(-10);
                }
                if(synergy.containsEdge(hero, ally)) {
                    hero.adjustRank(synergy.getEdge(hero, ally).weight()); // Recommend hero's that synergize with team
                }
                if(enemyTeam.size() == 0) {
                    Set<WeightedEdge> edges = counters.edgesOf(hero);
                    for(WeightedEdge e : edges) {
                        final Hero t = e.target();
                        if(counters.containsEdge(t, ally)) {
                            hero.adjustRank(1); // Recommend hero's that counter our team's counters
                        }
                        if(!coverage.contains(t)) {
                            hero.adjustRank(1); // Recommend hero's that extend our coverage
                        }
                    }
                }
            }
            for(Hero enemy : enemyTeam) {
                if(counters.containsEdge(hero, enemy)) {
                    if(!coverage.contains(enemy)) {
                        hero.adjustRank(2);
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
