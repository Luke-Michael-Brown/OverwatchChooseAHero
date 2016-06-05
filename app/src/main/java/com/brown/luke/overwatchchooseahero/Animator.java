package com.brown.luke.overwatchchooseahero;

public class Animator {
    private float current_value;
    private float start_value;
    private float end_value;
    private int duration;

    public Animator(float start_value, float end_value, int duration) {
        this.current_value = start_value;
        this.start_value = start_value;
        this.end_value = end_value;
        this.duration = duration;
    }

    public float getCurrentValue() {
        return current_value;
    }

    public void tick(float delta) {
        current_value += (end_value - start_value) * (delta / duration);
        if(start_value < end_value) {
            current_value = Math.min(current_value, end_value);
        } else {
            current_value = Math.max(current_value, end_value);
        }
    }

    public boolean isAnimating() {
        return current_value != end_value;
    }

}
