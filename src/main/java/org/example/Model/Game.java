package org.example.Model;

import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;

import java.util.ArrayList;

public class Game {
    private MapOfGame map;
    private ArrayList<User> players;
    private TimeAndDate timeAndDate;

    private WeatherType currentWeatherType;

    private ArrayList<NPC> npcs;

    public MapOfGame getMap() {
        return map;
    }
    public ArrayList<User> getPlayers() {
        return players;
    }
    public TimeAndDate getTimeAndDate() {
        return timeAndDate;
    }
    public ArrayList<NPC> getNpcs() {
        return npcs;
    }
    public WeatherType getCurrentWeatherType() {
        return currentWeatherType;
    }

    public void setMap(MapOfGame map) {
        this.map = map;
    }
    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }
    public void setTimeAndDate(TimeAndDate timeAndDate) {
        this.timeAndDate = timeAndDate;
    }
    public void setCurrentWeatherType(WeatherType currentWeatherType) {
        this.currentWeatherType = currentWeatherType;
    }
    public void setNpcs(ArrayList<NPC> npcs) {
        this.npcs = npcs;
    }
    public void createNPC(){}

}
