package com.brown.luke.overwatchchooseahero;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.HashMap;

public class Entity {
    // Id maps
    private static HashMap<String, Integer> SQ_MAP;
    private static HashMap<String, Integer> HEX_MAP;

    static {
        SQ_MAP = new HashMap<String, Integer>();
        SQ_MAP.put("genji", R.drawable.genji_sq);
        SQ_MAP.put("mcCree", R.drawable.mccree_sq);
        SQ_MAP.put("pharah", R.drawable.pharah_sq);
        SQ_MAP.put("reaper", R.drawable.reaper_sq);
        SQ_MAP.put("soldier76", R.drawable.soldier76_sq);
        SQ_MAP.put("tracer", R.drawable.tracer_sq);
        SQ_MAP.put("bastion", R.drawable.bastion_sq);
        SQ_MAP.put("hanzo", R.drawable.hanzo_sq);
        SQ_MAP.put("junkrat", R.drawable.junkrat_sq);
        SQ_MAP.put("mei", R.drawable.mccree_sq);
        SQ_MAP.put("torbjorn", R.drawable.torbjorn_sq);
        SQ_MAP.put("widowmaker", R.drawable.widowmaker_sq);
        SQ_MAP.put("dva", R.drawable.dva_sq);
        SQ_MAP.put("reinhardt", R.drawable.reinhardt_sq);
        SQ_MAP.put("roadhog", R.drawable.roadhog_sq);
        SQ_MAP.put("winston", R.drawable.widowmaker_sq);
        SQ_MAP.put("zarya", R.drawable.zarya_sq);
        SQ_MAP.put("lucio", R.drawable.lucio_sq);
        SQ_MAP.put("mercy", R.drawable.mei_sq);
        SQ_MAP.put("symmetra", R.drawable.symmetra_sq);
        SQ_MAP.put("zenyatta", R.drawable.zenyatta_sq);

        HEX_MAP = new HashMap<String, Integer>();
        HEX_MAP.put("genji", R.drawable.genji_hex);
        HEX_MAP.put("mcCree", R.drawable.mccree_hex);
        HEX_MAP.put("pharah", R.drawable.pharah_hex);
        HEX_MAP.put("reaper", R.drawable.reaper_hex);
        HEX_MAP.put("soldier76", R.drawable.soldier76_hex);
        HEX_MAP.put("tracer", R.drawable.tracer_hex);
        HEX_MAP.put("bastion", R.drawable.bastion_hex);
        HEX_MAP.put("hanzo", R.drawable.hanzo_hex);
        HEX_MAP.put("junkrat", R.drawable.junkrat_hex);
        HEX_MAP.put("mei", R.drawable.mccree_hex);
        HEX_MAP.put("torbjorn", R.drawable.torbjorn_hex);
        HEX_MAP.put("widowmaker", R.drawable.widowmaker_hex);
        HEX_MAP.put("dva", R.drawable.dva_hex);
        HEX_MAP.put("reinhardt", R.drawable.reinhardt_hex);
        HEX_MAP.put("roadhog", R.drawable.roadhog_hex);
        HEX_MAP.put("winston", R.drawable.widowmaker_hex);
        HEX_MAP.put("zarya", R.drawable.zarya_hex);
        HEX_MAP.put("lucio", R.drawable.lucio_hex);
        HEX_MAP.put("mercy", R.drawable.mei_hex);
        HEX_MAP.put("symmetra", R.drawable.symmetra_hex);
        HEX_MAP.put("zenyatta", R.drawable.zenyatta_hex);
        HEX_MAP.put("empty", R.drawable.empty_hex);
    }

    // Fields
    private String name;
    private EntityType type;
    private Bitmap bitmap;
    private PointF pos;

    public Entity(String name, EntityType type, float scale, Resources res) {
        this.pos = null;
        this.type = type;
        this.setImage(name, scale, res);

    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public PointF getPos() {
        return pos;
    }

    public void setImage(String name, float scale, Resources res) {
        this.name = name;
        Bitmap bm = BitmapFactory.decodeResource(res, (type == EntityType.SQ) ? SQ_MAP.get(name) : HEX_MAP.get(name));
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bm.getWidth(), bm.getHeight()), new RectF(0, 0, bm.getWidth() * scale, bm.getHeight() * scale), Matrix.ScaleToFit.CENTER);
        this.bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
    }

    public void setPos(PointF pos) {
        this.pos = pos;
    }

    public boolean contains(PointF poke) {
        return poke.x >= pos.x && poke.y >= pos.y &&
                poke.x <= pos.x + bitmap.getWidth() &&
                poke.y <= pos.y + bitmap.getHeight();
    }
}
