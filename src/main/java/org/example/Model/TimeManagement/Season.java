package org.example.Model.TimeManagement;

import java.util.ArrayList;

public enum Season {
    SPRING(new ArrayList<>()),
    SUMMER,
    AUTUMN,
    WINTER;

    ArrayList<WeatherType> weatherTypes;
    //ArrayList source (mixed seeds)
    //product class
    Season(ArrayList<WeatherType> weatherTypes) {}

    public ArrayList<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }
}
