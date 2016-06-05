package com.brown.luke.overwatchchooseahero;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.HashMap;

public abstract class Entity {
    private static Resources resources;

    // Fields
    private String name;
    private Bitmap bitmap;
    private PointF pos;

    public Entity(String name) {
        this.pos = null;
        this.setImage(name);

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

    public void setImage(String name) {
        this.name = name;
        Bitmap bm = BitmapFactory.decodeResource(resources, getIdMap().get(name));
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bm.getWidth(), bm.getHeight()), new RectF(0, 0, bm.getWidth() * getScale(), bm.getHeight() * getScale()), Matrix.ScaleToFit.CENTER);
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

    public static void setRes(Resources res) {
        Entity.resources = res;
    }


    abstract void update(float delta);
    abstract boolean isAnimating();
    abstract protected float getScale();
    abstract protected HashMap<String, Integer> getIdMap();
}
