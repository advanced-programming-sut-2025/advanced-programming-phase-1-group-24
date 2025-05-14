package org.example.Model;


import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Gift;
import org.example.Model.Friendships.Message;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.NPCManagement.NPCMission;
import org.example.Model.NPCManagement.NPCtype;
import org.example.Model.Reccepies.FoodRecipe;
import org.example.Model.TimeManagement.DayOfWeek;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;

import java.util.*;
import org.example.Model.MapManagement.*;

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
        generateNPCs();
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
    public void generateNPCs() {
        if (this.npcs == null) {
            this.npcs = new ArrayList<>();
        }
        Tile nextTile = this.map.getTile(72, 54);
        Map<String, Integer> requieredItems = new HashMap<>();
        Map<String, Integer> prize = new HashMap<>();
        requieredItems.put("Iron Bar", 50);
        prize.put("Diamond", 2);
        NPCMission sebastianMission1 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Pumpkin Pie", 1);
        prize.put("Gold Coin", 5000);
        NPCMission sebastianMission2 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Rock", 150);
        prize.put("Quartz", 50);
        NPCMission sebastianMission3 = new NPCMission(requieredItems, prize);
        ArrayList<NPCMission> sebastianMissions = new ArrayList<>();
        sebastianMissions.add(sebastianMission1);
        sebastianMissions.add(sebastianMission2);
        sebastianMissions.add(sebastianMission3);
        NPC sebastian = new NPC(NPCtype.Sebastian, players, sebastianMissions,30);
        npcs.add(sebastian);
        nextTile.setContainedNPC(sebastian);

        nextTile = this.map.getTile(72, 64);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Gold Bar", 1);
        prize.put("Friendship Level", 1);
        NPCMission abigailMission1 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Pumpkin", 1);
        prize.put("Gold Coin", 500);
        NPCMission abigailMission2 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wheat", 50);
        prize.put("Sprinkler", 1);
        NPCMission abigailMission3 = new NPCMission(requieredItems, prize);
        ArrayList<NPCMission> abigailMissions = new ArrayList<>();
        abigailMissions.add(abigailMission1);
        abigailMissions.add(abigailMission2);
        abigailMissions.add(abigailMission3);
        NPC abigail = new NPC(NPCtype.Abigail, players,abigailMissions,60);
        npcs.add(abigail);
        nextTile.setContainedNPC(abigail);

        nextTile = this.map.getTile(72, 74);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Strawberry", 12);
        prize.put("Gold Coin", 750);
        NPCMission harveyMission1 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Salmon", 1);
        prize.put("Friendship Level", 1);
        NPCMission harveyMission2 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wine", 1);
        prize.put("Salad", 5);
        NPCMission harveyMission3 = new NPCMission(requieredItems, prize);
        ArrayList<NPCMission> harveyMissions = new ArrayList<>();
        harveyMissions.add(harveyMission1);
        harveyMissions.add(harveyMission2);
        harveyMissions.add(harveyMission3);
        NPC harvey = new NPC(NPCtype.Harvey, players, harveyMissions,40);
        npcs.add(harvey);
        nextTile.setContainedNPC(harvey);

        nextTile = this.map.getTile(72, 84);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wood", 10);
        prize.put("Gold Coin", 500);
        NPCMission leahMission1 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Salmon", 1);
        prize.put("Salmon Dinner Recipe", 1);
        NPCMission leahMission2 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wood", 200);
        prize.put("Deluxe Scarecrow", 3);
        NPCMission leahMission3 = new NPCMission(requieredItems, prize);
        ArrayList<NPCMission> leahMissions = new ArrayList<>();
        leahMissions.add(leahMission1);
        leahMissions.add(leahMission2);
        leahMissions.add(leahMission3);
        NPC leah = new NPC(NPCtype.Leah, players, leahMissions,90);
        npcs.add(leah);
        nextTile.setContainedNPC(leah);

        nextTile = this.map.getTile(72, 94);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wood", 80);
        prize.put("Gold Coin", 1000);
        NPCMission robinMission1 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Iron Bar", 10);
        prize.put("Bee House", 3);
        NPCMission robinMission2 = new NPCMission(requieredItems, prize);
        requieredItems.clear();
        prize.clear();
        requieredItems.put("Wood", 1000);
        prize.put("Gold Coin", 25000);
        NPCMission robinMission3 = new NPCMission(requieredItems, prize);
        ArrayList<NPCMission> robinMissions = new ArrayList<>();
        robinMissions.add(robinMission1);
        robinMissions.add(robinMission2);
        robinMissions.add(robinMission3);
        NPC robin = new NPC(NPCtype.Robin, players, robinMissions,120);
        npcs.add(robin);
        nextTile.setContainedNPC(robin);
    }

    public NPC getNPC(String name) {
        for (NPC npc : npcs) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }

    public void handleFoodRecipe(User currentPlayer) {  //add this somewhere
        if (currentPlayer.getSkillsLevel().get(Skill.FORAGING) == 2 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.VegetableMedley))
            currentPlayer.getCookingRecepies().add(FoodRecipe.VegetableMedley);
        if (currentPlayer.getSkillsLevel().get(Skill.FARMING) == 1 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.FarmersLaunch))
            currentPlayer.getCookingRecepies().add(FoodRecipe.FarmersLaunch);
        if (currentPlayer.getSkillsLevel().get(Skill.FORAGING) == 3 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.SurvivalBurger))
            currentPlayer.getCookingRecepies().add(FoodRecipe.SurvivalBurger);
        if (currentPlayer.getSkillsLevel().get(Skill.FISHING) == 2 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.DishOtheSea))
            currentPlayer.getCookingRecepies().add(FoodRecipe.DishOtheSea);
        if (currentPlayer.getSkillsLevel().get(Skill.FISHING) == 3 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.SeaformPudding))
            currentPlayer.getCookingRecepies().add(FoodRecipe.SeaformPudding);
        if (currentPlayer.getSkillsLevel().get(Skill.MINING) == 1 &&
                !currentPlayer.getCookingRecepies().contains(FoodRecipe.MinersTreat))
            currentPlayer.getCookingRecepies().add(FoodRecipe.MinersTreat);
    }

}
