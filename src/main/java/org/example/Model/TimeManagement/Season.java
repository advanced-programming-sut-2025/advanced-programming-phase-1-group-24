package org.example.Model.TimeManagement;

import java.util.ArrayList;

public enum Season {
    SPRING(new ArrayList<>()),
    SUMMER(new ArrayList<>()),
    AUTUMN(new ArrayList<>()),
    WINTER(new ArrayList<>());

    ArrayList<WeatherType> weatherTypes;
    Season(ArrayList<WeatherType> weatherTypes) {}

    public ArrayList<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }
}
