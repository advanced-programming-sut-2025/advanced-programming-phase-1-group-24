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
    public static ToolMaterial fromString(String material) {
        if (material != null) {
            for (ToolMaterial toolMaterial : ToolMaterial.values()) {
                if (material.equalsIgnoreCase(toolMaterial.name())) {
                    return toolMaterial;
                }
            }
        }
       return null;
    }
    public ToolMaterial getNext() {
        switch (this) {
            case Initial:
                return Copper;
            case Copper:
                return Iron;
            case Iron:
                return Gold;
            case Gold:
                return Iridium;
            default:
                return null;  // No upgrade available for Initial or Iridium
        }
    }
}
