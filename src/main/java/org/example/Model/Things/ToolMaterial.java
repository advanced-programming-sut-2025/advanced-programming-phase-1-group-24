package org.example.Model.Things;

public enum ToolMaterial {
    Initial(5),
    Copper(4),
    Iron(3),
    Gold(2),
    Iridium(1);

    private final int energyRequiered;

    ToolMaterial(int energyRequiered) {
        this.energyRequiered = energyRequiered;
    }

    public int getEnergyRequiered() { return energyRequiered; }
}
