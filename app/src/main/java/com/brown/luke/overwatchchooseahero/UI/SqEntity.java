package com.brown.luke.overwatchchooseahero.UI;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.brown.luke.overwatchchooseahero.MainActivity;

public class SqEntity extends Entity {
    // Constants
    //-----------

    final short SQ_DURATION = 233;


    // Fields
    //---------

    private Animator xAnimator;
    private Animator yAnimator;


    // Constructor
    //-------------

    public SqEntity(final String name) {
        super(name);
        this.xAnimator = null;
        this.yAnimator = null;
    }


    // Setters
    //---------

    public void setAnimators(final PointF newPos) {
        xAnimator = new Animator(getPos().x, newPos.x, SQ_DURATION);
        yAnimator = new Animator(getPos().y, newPos.y, SQ_DURATION);
    }


    // Public methods
    //---------------

    @Override
    public void update(final float delta) {
        if(xAnimator != null && yAnimator != null) {
            xAnimator.tick(delta);
            yAnimator.tick(delta);
            setPos(new PointF(xAnimator.getCurrentValue(), yAnimator.getCurrentValue()));
            if(!xAnimator.isAnimating() && !yAnimator.isAnimating()) {
                xAnimator = null;
                yAnimator = null;
            }
        }
    }

    @Override
    public boolean isAnimating() {
        return xAnimator != null || yAnimator != null;
    }

    @Override
    protected float getScale() {
        return (float) MainActivity.getScreenSize().x / 785;
    }

    @Override
    protected String getSuffix() {
        return "sq";
    }
}
