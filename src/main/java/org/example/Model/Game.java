package org.example.Model;


import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;

import java.util.ArrayList;

public class Game {
    MapOfGame map;
    ArrayList<User> players;
    TimeAndDate timeAndDate;

    WeatherType currentWeatherType;

    ArrayList<NPC> npcs;
    public void createNPC(){}

}
