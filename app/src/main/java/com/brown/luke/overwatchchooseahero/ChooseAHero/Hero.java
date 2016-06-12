package com.brown.luke.overwatchchooseahero.ChooseAHero;

public class Hero {
    // Fields
    //--------

    private String name;
    private Role role;
    private SubRole subRole;
    private State preferedState;
    private boolean isCore;
    private int rank;


    // Constructors
    //--------------

    public Hero(String name, Role role) {
        this.name = name;
        this.role = role;
        this.subRole = null;
        if(role == Role.OFFENCE) {
            this.preferedState = State.ATTACK;
        } else if(role == Role.DEFENCE) {
            this.preferedState = State.DEFEND;
        }
        this.isCore = true;
        this.rank = 0;
    }
    public Hero(String name, Role role, boolean isCore) {
        this(name, role);
        this.isCore = isCore;
    }
    public Hero(String name, Role role, SubRole subRole) {
        this(name, role);
        this.subRole = subRole;
    }
    public Hero(String name, Role role, State preferedState) {
        this(name, role);
        this.preferedState = preferedState;
    }
    public Hero(String name, Role role, State preferedState, boolean isCore) {
        this(name, role);
        this.preferedState = preferedState;
        this.isCore = isCore;
    }
    public Hero(String name, Role role, State preferedState, SubRole subRole) {
        this(name, role, subRole);
        this.preferedState = preferedState;
    }
    public Hero(String name, Role role, SubRole subRole, boolean isCore) {
        this(name, role, subRole);
        this.isCore = isCore;
    }
    public Hero(String name, Role role, State preferedState, SubRole subRole, boolean isCore) {
        this(name, role, preferedState, subRole);
        this.isCore = isCore;
    }


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

    public void resetRank() {
        this.rank = 0;
    }

    public void adjustRank(int adjustment) {
        this.rank += adjustment;
    }

    public void adjustRank(double adjustment) {
        this.rank += adjustment;
    }

}
