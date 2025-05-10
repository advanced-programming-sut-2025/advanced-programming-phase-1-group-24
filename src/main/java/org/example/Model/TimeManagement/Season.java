package org.example.Model.TimeManagement;

import org.example.Model.Growables.SourceType;

import java.util.List;

public enum Season {
    SPRING(List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM)),

    SUMMER(List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM)),

    AUTUMN(List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM)),

    WINTER(List.of(WeatherType.SUNNY, WeatherType.SNOW));

    private final List<WeatherType> weatherTypes;

    Season(List<WeatherType> weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    public List<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }


    public Season next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}

