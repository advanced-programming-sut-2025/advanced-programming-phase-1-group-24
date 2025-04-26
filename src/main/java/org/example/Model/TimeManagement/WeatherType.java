package org.example.Model.TimeManagement;

public enum WeatherType {
    SUNNY,
    RAIN,
    STORM,
    SNOW;

    //Override the functions in each type!

    boolean automaticWatering;
    int energyOfToolsModifier;
    boolean causesLightning;


    WeatherType(boolean automaticWatering, int energyOfToolsModifier, boolean causesLightning) {
        this.automaticWatering = automaticWatering;
        this.energyOfToolsModifier = energyOfToolsModifier;
        this.causesLightning = causesLightning;

    }

    public int getEnergyOfToolsModifier() {
        return energyOfToolsModifier;
    }

    public boolean isAutomaticWatering() {
        return automaticWatering;
    }

    public boolean isCausesLightning() {
        return causesLightning;
    }

}

