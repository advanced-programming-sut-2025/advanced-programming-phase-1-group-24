package org.example.Model;


import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.TimeManagement.DayOfWeek;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;

import java.util.*;

public class Game {
    private MapOfGame map;
    private ArrayList<User> players;
    private TimeAndDate timeAndDate;
    private User currentPlayer;
    private User mainPlayer;  //the creator of the game or the last player that loaded the game
    private WeatherType currentWeatherType;
    private WeatherType tomorrowWeatherType;
    private int currentPlayerIndex = 0;
    int turnCounter = 0;

    private Map<User, Boolean> terminationVotes = new HashMap<>();
    private boolean isVoteInProgress = false;

    public Game(ArrayList<User> players, User mainPlayer, User currentPlayer) {
        this.players = players;
        this.mainPlayer = mainPlayer;
        this.currentPlayer = currentPlayer;
        this.timeAndDate = new TimeAndDate(9, 1, DayOfWeek.Saturday, Season.SPRING);
        this.map = new MapOfGame();
        this.currentWeatherType = WeatherType.SUNNY;
        predictTomorrowWeather();
    }

    private ArrayList<NPC> npcs;

    public List<User> getPlayers() {
        return players;
    }

    public boolean hasUser(User user) {
        return players.contains(user);
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public User getMainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(User mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public boolean isVoteInProgress() {
        return isVoteInProgress;
    }

    public void setVoteInProgress(boolean inProgress) {
        this.isVoteInProgress = inProgress;
    }


    public Map<User, Boolean> getTerminationVotes() {
        return terminationVotes;
    }

    public boolean isUserTurn(User user) {
        return currentPlayer == user;
        // Implement this to check if it's `user`'s turn
    }

    public void createNPC() {
    }


    public void advanceTimeByOneHour() {
        timeAndDate.advanceHour();
    }

    public MapOfGame getMap() {
        return map;
    }

    public void setMap(MapOfGame map) {
        this.map = map;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public TimeAndDate getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(TimeAndDate timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public WeatherType getCurrentWeatherType() {
        return currentWeatherType;
    }

    public void setCurrentWeatherType(WeatherType currentWeatherType) {
        this.currentWeatherType = currentWeatherType;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setTerminationVotes(Map<User, Boolean> terminationVotes) {
        this.terminationVotes = terminationVotes;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<NPC> npcs) {
        this.npcs = npcs;
    }

    public WeatherType getTomorrowWeatherType() {
        return tomorrowWeatherType;
    }

    public void setTomorrowWeatherType(WeatherType tomorrowWeatherType) {
        this.tomorrowWeatherType = tomorrowWeatherType;
    }

    public void predictTomorrowWeather() {
        Season currentSeason = timeAndDate.getSeason();
        List<WeatherType> possibleWeathers = currentSeason.getWeatherTypes();
        int randomIndex = new Random().nextInt(possibleWeathers.size());
        this.tomorrowWeatherType = possibleWeathers.get(randomIndex);
    }

}
