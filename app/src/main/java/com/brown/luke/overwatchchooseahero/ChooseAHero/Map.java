package com.brown.luke.overwatchchooseahero.ChooseAHero;


import java.util.ArrayList;

public class Map {
    private String name;
    private ArrayList<String> subMaps;

    public Map(final String name) {
        this.name = name;
        this.subMaps = null;
    }

    public Map(final String name, final ArrayList<String> subMaps) {
        this(name);
        this.subMaps = subMaps;
    }

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
