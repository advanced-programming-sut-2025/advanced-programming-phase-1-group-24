package org.example.Model;


import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.TimeManagement.DayOfWeek;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private MapOfGame map;
    private ArrayList<User> players;
    private TimeAndDate timeAndDate;
    private User currentPlayer;
    private User mainPlayer;  //the creator of the game or the last player that loaded the game
    private WeatherType currentWeatherType;
    private int currentPlayerIndex = 0;
    int turnCounter = 0;

    private Map<User, Boolean> terminationVotes = new HashMap<>();
    private boolean isVoteInProgress = false;

    public Game(ArrayList<User> players, User mainPlayer, User currentPlayer) {
        this.players = players;
        this.mainPlayer = mainPlayer;
        this.currentPlayer = currentPlayer;
        this.timeAndDate = new TimeAndDate(9, 1,DayOfWeek.Saturday, Season.SPRING);
    }

    ArrayList<NPC> npcs;

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

    public void goToNextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        turnCounter++;

        // Reset energy for the new player
        getCurrentPlayer().resetTurnEnergy();

        if (turnCounter == players.size()) {
            // One full round complete
            turnCounter = 0;
            advanceTimeByOneHour();
        }
    }
    public void advanceTimeByOneHour() {
        timeAndDate.advanceHour();
    }

}
