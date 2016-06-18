package com.brown.luke.overwatchchooseahero.OWRecommend;

public class Hero {
    // Fields
    //--------

    private String name = null;
    private Role role = null;
    private SubRole subRole = null;
    private State preferedState = null;
    private boolean isCore = true;
    private int rank = 0;


    // Getters
    //---------

    public String getName() {
        return this.name;
    }

    public Role getRole() {
        return this.role;
    }

    public boolean hasSubRole() {
        return this.subRole != null;
    }

    public SubRole getSubRole() {
        return this.subRole;
    }

    public State getPreferedState() {
        return this.preferedState;
    }

    public boolean getCore() {
        return isCore;
    }

    public int getRank() {
        return rank;
    }

    // Setters
    //---------

    public void setName(final String name) {
        this.name = name;
    }

    public void setRole(final Role role) {
        this.role = role;
        if(role == Role.OFFENCE) {
            this.preferedState = State.ATTACK;
        } else if(role == Role.DEFENCE) {
            this.preferedState = State.DEFEND;
        }
    }

    public void setSubRole(final SubRole subRole) {
        this.subRole = subRole;
    }

    public void setPreferedState(final State state) {
        this.preferedState = state;
    }

    public void setCore(final boolean core) {
        this.isCore = core;
    }

    public void resetRank() {
        this.rank = 0;
    }

    public void adjustRank(final int adjustment) {
        this.rank += adjustment;
    }

    public void adjustRank(final double adjustment) {
        this.rank += adjustment;
    }

}
