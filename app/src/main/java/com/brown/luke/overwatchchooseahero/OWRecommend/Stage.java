package com.brown.luke.overwatchchooseahero.OWRecommend;


import java.util.ArrayList;
import java.util.HashMap;

public class Stage {
    // Fields
    //--------

    private String name;
    private ArrayList<String> subMaps;
    private HashMap<String, HashMap<Hero, Integer>> ranks;

    // Constructors
    //-------------

    public Stage(final String name) {
        this.name = name;
        this.subMaps = new ArrayList<>();
        this.ranks = new HashMap<>();
    }

    // Getters
    //--------

    public String getName() {
        return name;
    }

    public ArrayList<String> getSubMaps() {
        return subMaps;
    }

    public int getRank(final String subMap, final Hero hero) {
        return ranks.get(subMap).get(hero);
    }

    // Setters
    //---------

    public void addSubMap(final String subMap) {
        subMaps.add(subMap);
        ranks.put(subMap, new HashMap<Hero, Integer>());
    }

    public void addRank(final String subMap, final Hero hero, final int amount) {
        ranks.get(subMap).put(hero, amount);
    }
}
