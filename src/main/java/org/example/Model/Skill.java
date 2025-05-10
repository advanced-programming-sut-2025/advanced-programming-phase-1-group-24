package org.example.Model;

public enum Skill {
    FARMING(5),
    MINING(10),
    FORAGING(10),
    FISHING(5);

    private final int xpPerAction;

    Skill(int xpPerAction) {
        this.xpPerAction = xpPerAction;
    }

    public int getXpPerAction() {
        return xpPerAction;
    }
}


