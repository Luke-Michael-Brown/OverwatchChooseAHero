package com.brown.luke.overwatchchooseahero.UI;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {
    // Static fields
    //---------------

    protected static Resources resources;
    protected static String packageName;


    // Fields
    //--------

    private String name;
    private Bitmap bitmap;
    private PointF pos;


    // Constructor
    //-------------

    public Entity(final String name) {
        this.pos = null;
        this.setImage(name);

    }


    // Getters
    //---------

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public PointF getPos() {
        return pos;
    }

    // Setters
    //--------

    public void setPos(final PointF pos) {
        this.pos = pos;
    }


    // Public methods
    //---------------

    public void setImage(final String name) {
        this.name = name;
        int resID = resources.getIdentifier(name + "_" + getSuffix(), "drawable", packageName);
        Bitmap bm = BitmapFactory.decodeResource(resources, resID);
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bm.getWidth(), bm.getHeight()), new RectF(0, 0, bm.getWidth() * getScale(), bm.getHeight() * getScale()), Matrix.ScaleToFit.CENTER);
        this.bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
    }

    public boolean contains(final PointF poke) {
        return poke.x >= pos.x && poke.y >= pos.y &&
                poke.x <= pos.x + bitmap.getWidth() &&
                poke.y <= pos.y + bitmap.getHeight();
    }

    public boolean isHover() {
        return false;
    }


    // Abstract methods
    //------------------

    abstract void update(final float delta);
    abstract boolean isAnimating();
    abstract protected float getScale();
    abstract protected String getSuffix();


    // Static methods
    //----------------

    public static void setRes(final Resources res, final String packageName) {
        Entity.resources = res;
        Entity.packageName = packageName;
    }
}
