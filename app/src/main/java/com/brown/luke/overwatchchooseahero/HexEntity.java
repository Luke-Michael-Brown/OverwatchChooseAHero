package com.brown.luke.overwatchchooseahero;


import android.content.res.Resources;

import java.util.HashMap;

public class HexEntity extends Entity {
    // Id map
    private static HashMap<String, Integer> HEX_MAP;

    static {
        HEX_MAP = new HashMap<String, Integer>();
        HEX_MAP.put("genji", R.drawable.genji_hex);
        HEX_MAP.put("mccree", R.drawable.mccree_hex);
        HEX_MAP.put("pharah", R.drawable.pharah_hex);
        HEX_MAP.put("reaper", R.drawable.reaper_hex);
        HEX_MAP.put("soldier76", R.drawable.soldier76_hex);
        HEX_MAP.put("tracer", R.drawable.tracer_hex);
        HEX_MAP.put("bastion", R.drawable.bastion_hex);
        HEX_MAP.put("hanzo", R.drawable.hanzo_hex);
        HEX_MAP.put("junkrat", R.drawable.junkrat_hex);
        HEX_MAP.put("mei", R.drawable.mei_hex);
        HEX_MAP.put("torbjorn", R.drawable.torbjorn_hex);
        HEX_MAP.put("widowmaker", R.drawable.widowmaker_hex);
        HEX_MAP.put("dva", R.drawable.dva_hex);
        HEX_MAP.put("reinhardt", R.drawable.reinhardt_hex);
        HEX_MAP.put("roadhog", R.drawable.roadhog_hex);
        HEX_MAP.put("winston", R.drawable.winston_hex);
        HEX_MAP.put("zarya", R.drawable.zarya_hex);
        HEX_MAP.put("lucio", R.drawable.lucio_hex);
        HEX_MAP.put("mercy", R.drawable.mercy_hex);
        HEX_MAP.put("symmetra", R.drawable.symmetra_hex);
        HEX_MAP.put("zenyatta", R.drawable.zenyatta_hex);
        HEX_MAP.put("empty", R.drawable.empty_hex);
    }

    public HexEntity(String name) {
        super(name);
    }

    @Override
    protected float getScale() {
        return 1;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean isAnimating() {
        return false;
    }



    @Override
    protected HashMap<String, Integer> getIdMap() {
        return HEX_MAP;
    }
}
