package com.brown.luke.overwatchchooseahero.OWRecommend;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.brown.luke.overwatchchooseahero.R;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class HeroDB {
    // Fields
    //--------

    private static Resources res;
    private static XmlResourceParser xpp;

    private static final ArrayList<Hero> heroes = new ArrayList<>();
    private static final ArrayList<String> defaultOrder = new ArrayList<>();
    private static final HashMap<String, Hero> nameMap = new HashMap<>();
    private static final SimpleDirectedWeightedGraph<Hero, WeightedEdge> counters = new SimpleDirectedWeightedGraph<>(WeightedEdge.class);
    private static final SimpleWeightedGraph<Hero, WeightedEdge> synergy = new SimpleWeightedGraph<>(WeightedEdge.class);


    // Getters
    //----------

    public static ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public static HashMap<String, Hero> getNameMap() {
        return nameMap;
    }

    public static ArrayList<String> getDefaultOrder() {
        return defaultOrder;
    }

    public static SimpleDirectedWeightedGraph<Hero, WeightedEdge> getCounters() {
        return counters;
    }

    public static SimpleWeightedGraph<Hero, WeightedEdge> getSynergy() {
        return synergy;
    }


    // Main methods
    //--------------

    public static void load(final Resources resources) {
        res = resources;

        loadHeroes();

        for (Hero hero : heroes) {
            counters.addVertex(hero);
            synergy.addVertex(hero);
        }

        loadCounters();
        loadSynergies();
    }

    private static void loadHeroes() {
        xpp = res.getXml( R.xml.herodata );

        try {
            int eventType = xpp.getEventType();
            String name = xpp.getName();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG && name.equals("Hero")) {
                    final Hero hero = new Hero();
                    for (byte i = 0; i < xpp.getAttributeCount(); ++i) {
                        String value = xpp.getAttributeValue(i);
                        switch (xpp.getAttributeName(i)) {
                            case "name":
                                hero.setName(value);
                                break;

                            case "role":
                                hero.setRole(Role.valueOf(value.toUpperCase()));
                                break;

                            case "subRole":
                                hero.setSubRole(SubRole.valueOf(value.toUpperCase()));
                                break;

                            case "perferedState":
                                hero.setPreferedState(State.valueOf(value.toUpperCase()));
                                break;

                            case "isCore":
                                hero.setCore(Boolean.valueOf(value));
                                break;
                        }
                    }

                    heroes.add(hero);
                    defaultOrder.add(hero.getName());
                    nameMap.put(hero.getName(), hero);
                }

                xpp.next();
                eventType = xpp.getEventType();
                name = xpp.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadCounters() {
        xpp = res.getXml( R.xml.counters );

        try {
            int eventType = xpp.getEventType();
            String name = xpp.getName();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG && name.equals("Counter")) {
                    String name1 = null;
                    String name2 = null;
                    int amount = 0;
                    for (byte i = 0; i < xpp.getAttributeCount(); ++i) {
                        switch (xpp.getAttributeName(i)) {
                            case "name1":
                                name1 = xpp.getAttributeValue(i);
                                break;

                            case "name2":
                                name2 = xpp.getAttributeValue(i);
                                break;

                            case "amount":
                                amount = xpp.getAttributeIntValue(i, 0);
                                break;
                        }
                    }

                    if(name1 != null && name2 != null && amount > 0) {
                        addCounter(nameMap.get(name1), nameMap.get(name2), amount);
                    }
                }

                xpp.next();
                eventType = xpp.getEventType();
                name = xpp.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadSynergies() {
        xpp = res.getXml( R.xml.counters );

        try {
            int eventType = xpp.getEventType();
            String name = xpp.getName();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG && name.equals("Synergy")) {
                    String name1 = null;
                    String name2 = null;
                    int amount = 0;
                    for (byte i = 0; i < xpp.getAttributeCount(); ++i) {
                        switch (xpp.getAttributeName(i)) {
                            case "name1":
                                name1 = xpp.getAttributeValue(i);
                                break;

                            case "name2":
                                name2 = xpp.getAttributeValue(i);
                                break;

                            case "amount":
                                amount = xpp.getAttributeIntValue(i, 0);
                                break;
                        }
                    }

                    if(name1 != null && name2 != null && amount > 0) {
                        addSynergy(nameMap.get(name1), nameMap.get(name2), amount);
                    }
                }

                xpp.next();
                eventType = xpp.getEventType();
                name = xpp.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Helpers
    //---------

    private static void addCounter(final Hero h1, final Hero h2, final int weight) {
        WeightedEdge e = counters.addEdge(h1, h2);
        counters.setEdgeWeight(e, weight);
    }

    private static void addSynergy(final Hero h1, final Hero h2, final int weight) {
        WeightedEdge e = synergy.addEdge(h1, h2);
        synergy.setEdgeWeight(e, weight);
    }
}
