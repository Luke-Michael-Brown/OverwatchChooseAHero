package com.brown.luke.overwatchchooseahero.OWRecommend;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * This is here because jgrapht is stupid
 */
public class WeightedEdge extends DefaultWeightedEdge {
    public Hero source() {
        return (Hero) super.getSource();
    }

    public Hero target() {
        return (Hero) super.getTarget();
    }

    public double weight() {
        return super.getWeight();
    }
}
