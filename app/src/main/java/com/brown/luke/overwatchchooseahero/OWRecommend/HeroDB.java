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
    private static final HashMap<String, Hero> heroNameMap = new HashMap<>();

    private static final SimpleDirectedWeightedGraph<Hero, WeightedEdge> counters = new SimpleDirectedWeightedGraph<>(WeightedEdge.class);
    private static final SimpleWeightedGraph<Hero, WeightedEdge> synergy = new SimpleWeightedGraph<>(WeightedEdge.class);

    private static final ArrayList<Stage> stages = new ArrayList<>();
    private static final HashMap<String, Stage> stageNameMap = new HashMap<>();

    // Getters
    //----------

    public static ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public static HashMap<String, Hero> getNameMap() {
        return heroNameMap;
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

    public static ArrayList<Stage> getStages() {
        return stages;
    }

    public static HashMap<String, Stage> getStageNameMap() {
        return stageNameMap;
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
        loadStages();
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

                            case "isCore":
                                hero.setCore(Boolean.valueOf(value));
                                break;
                        }
                    }

                    heroes.add(hero);
                    defaultOrder.add(hero.getName());
                    heroNameMap.put(hero.getName(), hero);
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
                        addCounter(heroNameMap.get(name1), heroNameMap.get(name2), amount);
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
                        addSynergy(heroNameMap.get(name1), heroNameMap.get(name2), amount);
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

    private static void loadStages() {
        xpp = res.getXml(R.xml.maps);

        try {
            Stage currentStage = null;
            String currentSubMap = null;

            int eventType = xpp.getEventType();
            String name = xpp.getName();
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG && name.equals("Map")) {
                    final String stageName = xpp.getAttributeValue(0);
                    currentStage = new Stage(stageName);
                    stages.add(currentStage);
                    stageNameMap.put(stageName, currentStage);
                }

                if (eventType == XmlResourceParser.START_TAG && name.equals("SubMap")) {
                    currentSubMap = xpp.getAttributeValue(0);
                    if(currentStage != null && currentSubMap != null) {
                        currentStage.addSubMap(currentSubMap);
                    }
                }

                if (eventType == XmlResourceParser.START_TAG && name.equals("Hero")) {
                    Hero hero = null;
                    int amount = 0;

                    for (byte i = 0; i < xpp.getAttributeCount(); ++i) {
                        switch (xpp.getAttributeName(i)) {
                            case "name":
                                hero = heroNameMap.get(xpp.getAttributeValue(i));
                                break;

                            case "amount":
                                amount = xpp.getAttributeIntValue(i, 0);
                                break;
                        }
                    }

                    if(currentStage != null && currentSubMap != null && hero != null) {
                        currentStage.addRank(currentSubMap, hero, amount);
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
