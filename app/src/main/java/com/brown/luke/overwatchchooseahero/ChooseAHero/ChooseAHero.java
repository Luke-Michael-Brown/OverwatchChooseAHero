package com.brown.luke.overwatchchooseahero.ChooseAHero;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ChooseAHero {
    // Fields
    private static ArrayList<Hero> heroes;
    private static ArrayList<Map> maps;
    private static SimpleDirectedWeightedGraph<Hero, WeightedEdge> counters;
    private static SimpleWeightedGraph<Hero, WeightedEdge> synergy;

    static  {
        // Setup maps
        final Map hanamura = new Map("Hanamura");
        final Map anubis = new Map("Anubis");
        final Map volskaya = new Map("Volskaya");
        final Map dorado = new Map("Dorado");
        final Map route66 = new Map("Route 66");
        final Map watchpoint = new Map("watchpoint");
        final Map hollywood = new Map("Hollywood");
        final Map kingsRow =  new Map("Kings Row");
        final Map numbani = new Map("Numbani");
        final Map ilios = new Map("Ilios", new ArrayList<>(Arrays.asList("Lighthouse", "Ruins", "Well")));
        final Map lijiang = new Map("Lijiang", new ArrayList<>(Arrays.asList("Control Center", "Garden", "Night Market")));
        final Map nepal =  new Map("Nepal", new ArrayList<>(Arrays.asList("Sanctum", "Shrine", "Village")));

        maps = new ArrayList<>();
        maps.add(hanamura);
        maps.add(anubis);
        maps.add(volskaya);
        maps.add(dorado);
        maps.add(route66);
        maps.add(watchpoint);
        maps.add(hollywood);
        maps.add(kingsRow);
        maps.add(numbani);
        maps.add(ilios);
        maps.add(lijiang);
        maps.add(nepal);

        // Setup heroes
        final Hero genji      = new Hero("genji", Role.OFFENCE, SubRole.FLANKER);
        final Hero mcCree     = new Hero("mccree", Role.OFFENCE, SubRole.ASSAULT);
        final Hero pharah     = new Hero("pharah", Role.OFFENCE, SubRole.ASSAULT);
        final Hero reaper     = new Hero("reaper", Role.OFFENCE, SubRole.FLANKER);
        final Hero soldier76  = new Hero("soldier76", Role.OFFENCE, SubRole.ASSAULT);
        final Hero tracer     = new Hero("tracer", Role.OFFENCE, SubRole.FLANKER);
        final Hero bastion    = new Hero("bastion", Role.DEFENCE, SubRole.BUILDER);
        final Hero hanzo      = new Hero("hanzo", Role.DEFENCE, SubRole.SNIPER);
        final Hero junkrat    = new Hero("junkrat", Role.DEFENCE);
        final Hero mei        = new Hero("mei", Role.DEFENCE);
        final Hero torbjorn   = new Hero("torbjorn", Role.DEFENCE, SubRole.BUILDER);
        final Hero widowmaker = new Hero("widowmaker", Role.DEFENCE, SubRole.SNIPER);
        final Hero dva        = new Hero("dva", Role.TANK, State.ATTACK, false);
        final Hero reinhardt  = new Hero("reinhardt", Role.TANK, State.ATTACK);
        final Hero roadhog    = new Hero("roadhog", Role.TANK, State.DEFEND);
        final Hero winston    = new Hero("winston", Role.TANK, State.ATTACK);
        final Hero zarya      = new Hero("zarya", Role.TANK, State.DEFEND);
        final Hero lucio      = new Hero("lucio", Role.SUPPORT, State.ATTACK);
        final Hero mercy      = new Hero("mercy", Role.SUPPORT, State.DEFEND);
        final Hero symmetra   = new Hero("symmetra", Role.SUPPORT, State.DEFEND, SubRole.BUILDER, false);
        final Hero zenyatta   = new Hero("zenyatta", Role.SUPPORT, State.ATTACK);

        // List heroes (in tier list order from best to worst)
        heroes = new ArrayList<Hero>();
        heroes.add(lucio);
        heroes.add(reinhardt);
        heroes.add(mcCree);
        heroes.add(mercy);
        heroes.add(reaper);
        heroes.add(widowmaker);
        heroes.add(winston);
        heroes.add(zarya);
        heroes.add(dva);
        heroes.add(genji);
        heroes.add(junkrat);
        heroes.add(pharah);
        heroes.add(roadhog);
        heroes.add(soldier76);
        heroes.add(tracer);
        heroes.add(bastion);
        heroes.add(hanzo);
        heroes.add(mei);
        heroes.add(torbjorn);
        heroes.add(symmetra);
        heroes.add(zenyatta);

        // Setup DAG for counters and UAG for synergy
        counters = new SimpleDirectedWeightedGraph<Hero, WeightedEdge>(WeightedEdge.class);
        synergy = new SimpleWeightedGraph<Hero, WeightedEdge>(WeightedEdge.class);
        for (Hero hero : heroes) {
            counters.addVertex(hero);
            synergy.addVertex(hero);
        }

        // Counters
        addCounter(genji, widowmaker, 3);
        addCounter(genji, bastion, 2);
        addCounter(genji, torbjorn, 1);
        addCounter(genji, hanzo, 2);
        addCounter(genji, zenyatta, 1);
        addCounter(genji, mercy, 1);

        addCounter(mcCree, tracer, 3);
        addCounter(mcCree, genji, 3);
        addCounter(mcCree, dva, 1);
        addCounter(mcCree, mercy, 2);
        addCounter(mcCree, pharah, 1);
        addCounter(mcCree, reaper, 1);
        addCounter(mcCree, torbjorn, 1);
        addCounter(mcCree, bastion, 1);
        addCounter(mcCree, winston, 1);
        addCounter(mcCree, lucio, 3);
        addCounter(mcCree, soldier76, 1);

        addCounter(pharah, torbjorn, 2);
        addCounter(pharah, bastion, 2);
        addCounter(pharah, reinhardt, 3);
        addCounter(pharah, mei, 2);
        addCounter(pharah, junkrat, 2);
        addCounter(pharah, lucio, 1);
        addCounter(pharah, genji, 1);
        addCounter(pharah, symmetra, 2);
        addCounter(pharah, tracer, 1);
        addCounter(pharah, reaper, 1);
        addCounter(pharah, zarya, 1);

        addCounter(reaper, zarya, 3);
        addCounter(reaper, winston, 3);
        addCounter(reaper, widowmaker, 1);
        addCounter(reaper, tracer, 1);
        addCounter(reaper, reinhardt, 2);
        addCounter(reaper, dva, 2);
        addCounter(reaper, zenyatta, 2);
        addCounter(reaper, junkrat, 1);
        addCounter(reaper, roadhog, 1);
        addCounter(reaper, genji, 1);
        addCounter(reaper, mcCree, 1);

        addCounter(soldier76, pharah, 3);
        addCounter(soldier76, roadhog, 1);
        addCounter(soldier76, bastion, 1);
        addCounter(soldier76, torbjorn, 1);
        addCounter(soldier76, tracer, 1);
        addCounter(soldier76, mei, 2);
        addCounter(soldier76, mercy, 1);

        addCounter(tracer, widowmaker, 2);
        addCounter(tracer, hanzo, 2);
        addCounter(tracer, bastion, 1);
        addCounter(tracer, torbjorn, 1);
        addCounter(tracer, mercy, 2);
        addCounter(tracer, zenyatta, 2);
        addCounter(tracer, lucio, 1);

        addCounter(bastion, zarya, 2);
        addCounter(bastion, reaper, 1);
        addCounter(bastion, mercy, 1);
        addCounter(bastion, lucio, 1);
        addCounter(bastion, dva, 1);
        addCounter(bastion, winston, 2);

        addCounter(hanzo, mercy, 1);
        addCounter(hanzo, zenyatta, 2);
        addCounter(hanzo, mcCree, 2);
        addCounter(hanzo, bastion, 1);
        addCounter(hanzo, pharah, 1);
        addCounter(hanzo, symmetra, 1);
        addCounter(hanzo, torbjorn, 1);
        addCounter(hanzo, junkrat, 1);

        addCounter(junkrat, mei, 1);
        addCounter(junkrat, reinhardt, 1);
        addCounter(junkrat, roadhog, 1);
        addCounter(junkrat, symmetra, 2);
        addCounter(junkrat, bastion, 1);
        addCounter(junkrat, torbjorn, 1);
        addCounter(junkrat, dva, 1);

        addCounter(widowmaker, mercy, 1);
        addCounter(widowmaker, zenyatta, 3);
        addCounter(widowmaker, mcCree, 2);
        addCounter(widowmaker, soldier76, 2);
        addCounter(widowmaker, bastion, 3);
        addCounter(widowmaker, torbjorn, 3);
        addCounter(widowmaker, hanzo, 1);
        addCounter(widowmaker, roadhog, 1);
        addCounter(widowmaker, pharah, 1);
        addCounter(widowmaker, lucio, 1);

        addCounter(mei, genji, 3);
        addCounter(mei, widowmaker, 1);
        addCounter(mei, hanzo, 1);
        addCounter(mei, lucio, 3);
        addCounter(mei, symmetra, 1);
        addCounter(mei, torbjorn, 1);
        addCounter(mei, bastion, 1);
        addCounter(mei, dva, 1);
        addCounter(mei, reinhardt, 1);
        addCounter(mei, winston, 1);
        addCounter(mei, reaper, 1);

        addCounter(torbjorn, roadhog, 2);
        addCounter(torbjorn, lucio, 1);
        addCounter(torbjorn, mercy, 1);
        addCounter(torbjorn, reaper, 1);

        addCounter(dva, widowmaker, 2);
        addCounter(dva, hanzo, 1);
        addCounter(dva, torbjorn, 1);
        addCounter(dva, soldier76, 1);
        addCounter(dva, pharah, 1);
        addCounter(dva, mercy, 1);
        addCounter(dva, roadhog, 1);

        addCounter(reinhardt, widowmaker, 2);
        addCounter(reinhardt, hanzo, 2);
        addCounter(reinhardt, torbjorn, 1);
        addCounter(reinhardt, soldier76, 1);
        addCounter(reinhardt, junkrat, 1);

        addCounter(roadhog, dva, 2);
        addCounter(roadhog, pharah, 1);
        addCounter(roadhog, genji, 1);
        addCounter(roadhog, tracer, 1);
        addCounter(roadhog, mercy, 2);
        addCounter(roadhog, zenyatta, 2);
        addCounter(roadhog, lucio, 3);
        addCounter(roadhog, reinhardt, 1);
        addCounter(roadhog, soldier76, 1);
        addCounter(roadhog, winston, 1);

        addCounter(winston, widowmaker, 2);
        addCounter(winston, hanzo, 2);
        addCounter(winston, genji, 3);
        addCounter(winston, tracer, 2);
        addCounter(winston, reinhardt, 1);
        addCounter(winston, symmetra, 2);
        addCounter(winston, junkrat, 2);

        addCounter(zarya, dva, 2);
        addCounter(zarya, winston, 2);
        addCounter(zarya, reinhardt, 1);
        addCounter(zarya, genji, 1);
        addCounter(zarya, mcCree, 3);
        addCounter(zarya, mei, 1);
        addCounter(zarya, junkrat, 1);
        addCounter(zarya, symmetra, 1);

        addCounter(lucio, dva, 3);
        addCounter(lucio, reaper, 1);
        addCounter(lucio, soldier76, 1);

        addCounter(mercy, reaper, 1);
        addCounter(mercy, winston, 1);
        addCounter(mercy, pharah, 1);
        addCounter(mercy, junkrat, 1);

        addCounter(symmetra, dva, 1);
        addCounter(symmetra, genji, 2);
        addCounter(symmetra, tracer, 2);
        addCounter(symmetra, reaper, 2);
        addCounter(symmetra, reinhardt, 2);

        addCounter(zenyatta, dva, 1);
        addCounter(zenyatta, roadhog, 2);
        addCounter(zenyatta, reinhardt, 1);
        addCounter(zenyatta, winston, 1);
        addCounter(zenyatta, bastion, 1);

        // Synergy
        addSynergy(reinhardt, mcCree, 2);
        addSynergy(winston, junkrat, 2);
        addSynergy(zarya, tracer, 1);
        addSynergy(zarya, pharah, 1);
        addSynergy(zarya, mcCree, 1);
        addSynergy(zarya, dva, 1);
        addSynergy(soldier76, zenyatta, 2);
        addSynergy(roadhog, reaper, 1);
        addSynergy(reinhardt, lucio, 2);
        addSynergy(reinhardt, mercy, 1);
        addSynergy(reinhardt, hanzo, 1);
        addSynergy(pharah, mercy, 2);
        addSynergy(roadhog, junkrat, 1);
        addSynergy(reaper, lucio, 2);
        addSynergy(mercy, genji, 1);
        addSynergy(mcCree, lucio, 2);
        addSynergy(mercy, bastion, 1);
        addSynergy(mercy, widowmaker, 1);
        addSynergy(mercy, tracer, 1);
        addSynergy(roadhog, lucio, 2);
        addSynergy(zarya, lucio, 2);
        addSynergy(lucio, mei, 2);
        addSynergy(mei, reinhardt, 2);
        addSynergy(zenyatta, symmetra, 2);
        addSynergy(lucio, symmetra, 1);
        addSynergy(mercy, symmetra, 1);
    }

    // Main algorithm
    public static ArrayList<String> run(final ArrayList<String> allyTeamStrings, final ArrayList<String> enemyTeamStrings, final String stateString) {
        ArrayList<Hero> allyTeam = new ArrayList<>();
        ArrayList<Hero> enemyTeam = new ArrayList<>();
        for(Hero hero : heroes) {
            for(String allyName : allyTeamStrings) {
                if(allyName.equals(hero.getName())) {
                    allyTeam.add(hero);
                }
            }
            for(String enemyName : enemyTeamStrings) {
                if(enemyName.equals(hero.getName())) {
                    enemyTeam.add(hero);
                }
            }
        }

        State state;
        switch (stateString) {
            case "Attack":
                state = State.ATTACK;
                break;
            case "Defend":
                state = State.DEFEND;
                break;
            case "Control":
                state = State.KOH;
                break;
            default:
                return null;
        }

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

            if(hero.getPreferedState() == state) {
                hero.adjustRank(4);
            }

            // If we are on attack/defend and they are an attack/defend unit +1/-1
            if(hero.getRole() == Role.OFFENCE) {
                if(state == State.ATTACK) {
                    hero.adjustRank(1);
                } else if(state == State.DEFEND) {
                    hero.adjustRank(-1);
                }
            } else if(hero.getRole() == Role.DEFENCE) {
                if(state == State.ATTACK) {
                    hero.adjustRank(-1);
                } else if(state == State.DEFEND) {
                    hero.adjustRank(1);
                }
            }

            for(Hero ally : allyTeam) {
                if(hero == ally) {
                    hero.adjustRank(-5);
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

    public static ArrayList<String> getSubMaps(String mapName) {
        for(Map map : maps) {
            if(map.getName().equals(mapName)) {
                return map.getSubMaps();
            }
        }

        return null;
    }

    // Helpers
    private static void addCounter(Hero h1, Hero h2, int weight) {
        WeightedEdge e = counters.addEdge(h1, h2);
        counters.setEdgeWeight(e, weight);
    }

    private static void addSynergy(Hero h1, Hero h2, int weight) {
        WeightedEdge e = synergy.addEdge(h1, h2);
        synergy.setEdgeWeight(e, weight);
    }
}
