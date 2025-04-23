package org.example.Model.TimeManagement;

public enum WeatherType {
    SUNNY,
    RAIN,
    STORM,
    SNOW;

    //Override the functions in each type!

    boolean automaticWatering;
    int energyOfToolsModifier;
    boolean destroysCrops;
    boolean causesLightning;


    WeatherType(boolean automaticWatering, int energyOfToolsModifier, boolean destroyCrops, boolean causesLightning) {
        this.automaticWatering = automaticWatering;
        this.energyOfToolsModifier = energyOfToolsModifier;
        this.destroysCrops = destroyCrops;
        this.causesLightning = causesLightning;

    }

    public int getEnergyOfToolsModifier() {
        return energyOfToolsModifier;
    }

    public boolean isAutomaticWatering() {
        return automaticWatering;
    }

    public boolean isDestroysCrops() {
        return destroysCrops;
    }

    public boolean isCausesLightning() {
        return causesLightning;
    }

}

