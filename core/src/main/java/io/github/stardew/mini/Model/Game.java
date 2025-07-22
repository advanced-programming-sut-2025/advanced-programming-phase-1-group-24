package io.github.stardew.mini.Model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Friendships.Friendship;
import io.github.stardew.mini.Model.MapManagement.*;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.NPCManagement.NPC;
import io.github.stardew.mini.Model.NPCManagement.NPCMission;
import io.github.stardew.mini.Model.NPCManagement.NPCtype;
import io.github.stardew.mini.Model.Places.Farm;
import io.github.stardew.mini.Model.Reccepies.FoodRecipe;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.TimeManagement.TimeAndDate;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.Places.Habitat;
import java.util.*;
import io.github.stardew.mini.Model.MapManagement.*;
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")

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

    private Map<String, List<NPCMission>> playerAddedMissions = new HashMap<>();

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
    public Game() {
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
        NPC sebastian = new NPC(NPCtype.Sebastian, players, new ArrayList<>(Arrays.asList(
            new NPCMission(Map.of("Iron Bar", 50), Map.of("Diamond", 2)),
            new NPCMission(Map.of("Pumpkin Pie", 1), Map.of("Gold Coin", 5000)),
            new NPCMission(Map.of("Rock", 150), Map.of("Quartz", 50))
        )), 30);
        npcs.add(sebastian);
        Tile sebastianHomeTile = map.getTile(sebastian.getNpcName().getHomeLocation().x, sebastian.getNpcName().getHomeLocation().y);
        if (sebastianHomeTile != null) {
            sebastian.setCurrentTile(sebastianHomeTile);
            sebastianHomeTile.setContainedNPC(sebastian);
        }

        NPC abigail = new NPC(NPCtype.Abigail, players, new ArrayList<>(Arrays.asList(
            new NPCMission(Map.of("Gold Bar", 1), Map.of("Friendship Level", 1)),
            new NPCMission(Map.of("Pumpkin", 1), Map.of("Gold Coin", 500)),
            new NPCMission(Map.of("Wheat", 50), Map.of("Sprinkler", 1))
        )), 60);
        npcs.add(abigail);
        Tile abigailHomeTile = map.getTile(abigail.getNpcName().getHomeLocation().x, abigail.getNpcName().getHomeLocation().y);
        if (abigailHomeTile != null) {
            abigail.setCurrentTile(abigailHomeTile);
            abigailHomeTile.setContainedNPC(abigail);
        }

        NPC harvey = new NPC(NPCtype.Harvey, players, new ArrayList<>(Arrays.asList(
            new NPCMission(Map.of("Strawberry", 12), Map.of("Gold Coin", 750)),
            new NPCMission(Map.of("Salmon", 1), Map.of("Friendship Level", 1)),
            new NPCMission(Map.of("Wine", 1), Map.of("Salad", 5))
        )), 40);
        npcs.add(harvey);
        Tile harveyHomeTile = map.getTile(harvey.getNpcName().getHomeLocation().x, harvey.getNpcName().getHomeLocation().y);
        if (harveyHomeTile != null) {
            harvey.setCurrentTile(harveyHomeTile);
            harveyHomeTile.setContainedNPC(harvey);
        }

        NPC leah = new NPC(NPCtype.Leah, players, new ArrayList<>(Arrays.asList(
            new NPCMission(Map.of("Wood", 10), Map.of("Gold Coin", 500)),
            new NPCMission(Map.of("Salmon", 1), Map.of("Salmon Dinner Recipe", 1)),
            new NPCMission(Map.of("Wood", 200), Map.of("Deluxe Scarecrow", 3))
        )), 90);
        npcs.add(leah);
        Tile leahHomeTile = map.getTile(leah.getNpcName().getHomeLocation().x, leah.getNpcName().getHomeLocation().y);
        if (leahHomeTile != null) {
            leah.setCurrentTile(leahHomeTile);
            leahHomeTile.setContainedNPC(leah);
        }

        NPC robin = new NPC(NPCtype.Robin, players, new ArrayList<>(Arrays.asList(
            new NPCMission(Map.of("Wood", 80), Map.of("Gold Coin", 1000)),
            new NPCMission(Map.of("Iron Bar", 10), Map.of("Bee House", 3)),
            new NPCMission(Map.of("Wood", 1000), Map.of("Gold Coin", 25000))
        )), 120);
        npcs.add(robin);
        Tile robinHomeTile = map.getTile(robin.getNpcName().getHomeLocation().x, robin.getNpcName().getHomeLocation().y);
        if (robinHomeTile != null) {
            robin.setCurrentTile(robinHomeTile);
            robinHomeTile.setContainedNPC(robin);
        }
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
    public void reloadExtraData(){
        for (Tile[] row : map.getMap()) {
            for (Tile tile : row) {
                Animal animal = tile.getContainedAnimal();
                if (animal != null) {
                    animal.reloadAfterLoad(tile);
                }
            }
        }
    }

    public Map<String, List<NPCMission>> getPlayerAddedMissions() {
        return playerAddedMissions;
    }
}
