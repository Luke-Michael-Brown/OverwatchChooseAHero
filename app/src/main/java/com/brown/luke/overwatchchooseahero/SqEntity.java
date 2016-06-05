package com.brown.luke.overwatchchooseahero;

import android.graphics.PointF;
import java.util.HashMap;

public class SqEntity extends Entity {
    // Constants
    final int SQ_DURATION = 300;

    // Id map
    private static HashMap<String, Integer> SQ_MAP;

    static {
        SQ_MAP = new HashMap<String, Integer>();
        SQ_MAP.put("genji", R.drawable.genji_sq);
        SQ_MAP.put("mccree", R.drawable.mccree_sq);
        SQ_MAP.put("pharah", R.drawable.pharah_sq);
        SQ_MAP.put("reaper", R.drawable.reaper_sq);
        SQ_MAP.put("soldier76", R.drawable.soldier76_sq);
        SQ_MAP.put("tracer", R.drawable.tracer_sq);
        SQ_MAP.put("bastion", R.drawable.bastion_sq);
        SQ_MAP.put("hanzo", R.drawable.hanzo_sq);
        SQ_MAP.put("junkrat", R.drawable.junkrat_sq);
        SQ_MAP.put("mei", R.drawable.mei_sq);
        SQ_MAP.put("torbjorn", R.drawable.torbjorn_sq);
        SQ_MAP.put("widowmaker", R.drawable.widowmaker_sq);
        SQ_MAP.put("dva", R.drawable.dva_sq);
        SQ_MAP.put("reinhardt", R.drawable.reinhardt_sq);
        SQ_MAP.put("roadhog", R.drawable.roadhog_sq);
        SQ_MAP.put("winston", R.drawable.winston_sq);
        SQ_MAP.put("zarya", R.drawable.zarya_sq);
        SQ_MAP.put("lucio", R.drawable.lucio_sq);
        SQ_MAP.put("mercy", R.drawable.mercy_sq);
        SQ_MAP.put("symmetra", R.drawable.symmetra_sq);
        SQ_MAP.put("zenyatta", R.drawable.zenyatta_sq);
    }

    // Fields
    private Animator xAnimator;
    private Animator yAnimator;

    public SqEntity(String name) {
        super(name);
        this.xAnimator = null;
        this.yAnimator = null;
    }

    public void setAnimators(PointF newPos) {
        xAnimator = new Animator(getPos().x, newPos.x, SQ_DURATION);
        yAnimator = new Animator(getPos().y, newPos.y, SQ_DURATION);
    }

    @Override
    public void update(float delta) {
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
        return 1.5f;
    }

    @Override
    protected HashMap<String, Integer> getIdMap() {
        return SQ_MAP;
    }
}
