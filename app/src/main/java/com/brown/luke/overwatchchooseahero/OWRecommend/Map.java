package com.brown.luke.overwatchchooseahero.OWRecommend;


import java.util.ArrayList;

public class Map {
    // Fields
    //--------

    private String name;
    private ArrayList<String> subMaps;

    // Constructors
    //-------------

    public Map(final String name) {
        this.name = name;
        this.subMaps = null;
    }

    public Map(final String name, final ArrayList<String> subMaps) {
        this(name);
        this.subMaps = subMaps;
    }


    // Getters
    //--------

    public String getName() {
        return name;
    }

    public ArrayList<String> getSubMaps() {
        return subMaps;
    }

    public boolean isKoh() {
        return subMaps != null;
    }
}
