package com.brown.luke.overwatchchooseahero.UI;

public class Animator {
    // Fields
    //--------

    private float current_value;
    private float start_value;
    private float end_value;
    private int duration;

    // Constructor
    //-------------

    public Animator(final float start_value, final float end_value, final int duration) {
        this.current_value = start_value;
        this.start_value = start_value;
        this.end_value = end_value;
        this.duration = duration;
    }

    // Getters
    //--------

    public float getCurrentValue() {
        return current_value;
    }

    public boolean isAnimating() {
        return current_value != end_value;
    }


    // Public method
    //----------------

    public void tick(final float delta) {
        current_value += (end_value - start_value) * (delta / duration);
        if(start_value < end_value) {
            current_value = Math.min(current_value, end_value);
        } else {
            current_value = Math.max(current_value, end_value);
        }
    }
}
