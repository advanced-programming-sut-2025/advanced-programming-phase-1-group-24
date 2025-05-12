package org.example.Model;


import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Gift;
import org.example.Model.Friendships.Message;
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

    private ArrayList<Message> allMessages = new ArrayList<>();
   // private ArrayList<Gift> allGifts = new ArrayList<>();
    private ArrayList<Friendship> allFriendships = new ArrayList<>();

    public Game(ArrayList<User> players, User mainPlayer, User currentPlayer) {
        this.players = players;
        this.mainPlayer = mainPlayer;
        this.currentPlayer = currentPlayer;
        this.timeAndDate = new TimeAndDate(9, 1, DayOfWeek.Saturday, Season.SPRING);
        this.map = new MapOfGame();
        this.currentWeatherType = WeatherType.SUNNY;
        for (int i = 0; i < players.size(); i++) {
            User player1 = players.get(i);
            for (int j = i + 1; j < players.size(); j++) {
                User player2 = players.get(j);

                Friendship friendship = new Friendship(player1.getUsername(), player2.getUsername());

                allFriendships.add(friendship);
            }
        }

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

    public ArrayList<Message> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(ArrayList<Message> allMessages) {
        this.allMessages = allMessages;
    }

//    public ArrayList<Gift> getAllGifts() {
//        return allGifts;
//    }
//
//    public void setAllGifts(ArrayList<Gift> allGifts) {
//        this.allGifts = allGifts;
//    }
    public User getPlayerByUsername(String username) {
        for (User player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }
    public Friendship getFriendship(String name1, String name2) {
        // Ensure consistent ordering as used in Friendship constructor
        String player1 = name1.compareTo(name2) < 0 ? name1 : name2;
        String player2 = name1.compareTo(name2) < 0 ? name2 : name1;

        for (Friendship friendship : allFriendships) {
            if (friendship.getPlayer1().equals(player1) && friendship.getPlayer2().equals(player2)) {
                return friendship;
            }
        }
        return null; // Or throw exception if preferred
    }

    public ArrayList<Friendship> getAllFriendships() {
        return allFriendships;
    }
}
