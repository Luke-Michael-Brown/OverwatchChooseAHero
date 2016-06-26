package com.brown.luke.overwatchchooseahero.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

import com.brown.luke.overwatchchooseahero.MainActivity;

public class HexEntity extends Entity {
    // Fields
    //--------

    private Bitmap hoverBitmap;


    // Constructor
    //-------------

    public HexEntity(final String name) {
        super(name);
        this.hoverBitmap = null;
    }


    // Getters
    //--------


    @Override
    public Bitmap getBitmap() {
        if(this.hoverBitmap == null) {
            return super.getBitmap();
        } else {
            return this.hoverBitmap;
        }
    }


    // Public methods
    //-----------------

    public void setHoverBitmap(final String hoverName) {
        int resID = MainActivity.getRes().getIdentifier(hoverName + "_" + getSuffix(), "drawable", MainActivity.getPName());
        Bitmap bm = BitmapFactory.decodeResource(MainActivity.getRes(), resID);
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bm.getWidth(), bm.getHeight()), new RectF(0, 0, bm.getWidth() * getScale(), bm.getHeight() * getScale()), Matrix.ScaleToFit.CENTER);
        this.hoverBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
    }

    public void removeHoverBitmap() {
        this.hoverBitmap = null;
    }

    @Override
    public void setImage(final String name) {
        super.setImage(name);
        this.hoverBitmap = null;
    }

    @Override
    public boolean isHover() {
        return this.hoverBitmap != null;
    }

    @Override
    protected float getScale() {
        return (float) MainActivity.getScreenSize().x / 1250;
    }

    @Override
    public void update(final float delta) {}

    @Override
    public boolean isAnimating() {
        return false;
    }

    @Override
    protected String getSuffix() {
        return "hex";
    }
}
