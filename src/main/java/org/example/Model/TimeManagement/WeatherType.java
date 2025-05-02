package org.example.Model.TimeManagement;

public enum WeatherType {
    SUNNY(false, 1.0, false),
    RAIN(true, 1.5, false),
    STORM(true, 1.5, true),
    SNOW(false, 2.0, false);

    //Override the functions in each type!

    private final boolean automaticWatering;
    private final double energyOfToolsModifier;
    private final boolean causesLightning;


    WeatherType(boolean automaticWatering, double energyOfToolsModifier, boolean causesLightning) {
        this.automaticWatering = automaticWatering;
        this.energyOfToolsModifier = energyOfToolsModifier;
        this.causesLightning = causesLightning;

    }

    public double getEnergyOfToolsModifier() {
        return energyOfToolsModifier;
    }

    public boolean isAutomaticWatering() {
        return automaticWatering;
    }


    public boolean isCausesLightning() {
        return causesLightning;
    }


}

