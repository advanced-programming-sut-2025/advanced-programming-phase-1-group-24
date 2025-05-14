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

    public static Skill fromString(String skillString) {
        for (Skill skill : Skill.values()) {
            if (skillString.equalsIgnoreCase(skill.name())) {
                return skill;
            }
        }
        return null;
    }
    public int getXpPerAction() {
        return xpPerAction;
    }
}


