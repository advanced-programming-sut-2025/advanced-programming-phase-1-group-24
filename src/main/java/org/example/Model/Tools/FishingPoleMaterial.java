package org.example.Model.Tools;

public enum FishingPoleMaterial {
    Training(0.1,8),
    Bamboo(0.5,8),
    FiberGlass(0.9,6),
    Iridium(1.2,4);

    private final  double fishQuality;
    private final  int energyRequired;

    FishingPoleMaterial(double fishQuality, int energyRequired) {
        this.fishQuality = fishQuality;
        this.energyRequired = energyRequired;
    }

    public double getFishQuality() { return fishQuality; }
    public int getEnergyRequired() { return energyRequired; }
}
