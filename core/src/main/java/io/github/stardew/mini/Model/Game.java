package io.github.stardew.mini.Model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Friendships.Friendship;
import io.github.stardew.mini.Model.Friendships.Trade;
import io.github.stardew.mini.Model.MapManagement.*;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.NPCManagement.NPC;
import io.github.stardew.mini.Model.NPCManagement.NPCMission;
import io.github.stardew.mini.Model.NPCManagement.NPCtype;
import io.github.stardew.mini.Model.Places.Farm;
import io.github.stardew.mini.Model.Places.House;
import io.github.stardew.mini.Model.Reccepies.FoodRecipe;
import io.github.stardew.mini.Model.Reccepies.Machine;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.TimeManagement.TimeAndDate;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.Places.Habitat;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.github.stardew.mini.Model.MapManagement.*;
import io.github.stardew.mini.client.MainApp;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")

public class Game {
    private String NetworkId = UUID.randomUUID().toString();
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
    private Map<String, Boolean> mapSelectionStatus = new HashMap<>();
    private Map<String, Boolean> loadStatus = new HashMap<>();

    private boolean voteOutInProgress = false;
    private User playerToVoteOut;
    private Map<User, Boolean> voteOutVotes = new HashMap<>();

    private final Map<String, Trade> activeTrades = new ConcurrentHashMap<>();

    private final List<Point> treePoints = new ArrayList<>();
    private final List<Point> bushPoints = new ArrayList<>();

    private boolean waitingForPlayersToSleep = false;

    public boolean isWaitingForPlayersToSleep() {
        return waitingForPlayersToSleep;
    }

    public void setWaitingForPlayersToSleep(boolean waitingForPlayersToSleep) {
        this.waitingForPlayersToSleep = waitingForPlayersToSleep;
    }

    public Game(ArrayList<User> players, User mainPlayer, User currentPlayer) {
        this.players = players;
        for (User player : players) {
            mapSelectionStatus.put(player.getUsername(), false);
            loadStatus.put(player.getUsername(), false);
        }
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
        generateScenery();
        for (User player : players) {
            playerAddedMissions.put(player.getUsername(), new ArrayList<>());
        }
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

    public boolean isVoteOutInProgress() {
        return voteOutInProgress;
    }

    public void setVoteOutInProgress(boolean voteOutInProgress) {
        this.voteOutInProgress = voteOutInProgress;
    }

    public User getPlayerToVoteOut() {
        return playerToVoteOut;
    }

    public void setPlayerToVoteOut(User playerToVoteOut) {
        this.playerToVoteOut = playerToVoteOut;
    }

    public Map<User, Boolean> getVoteOutVotes() {
        return voteOutVotes;
    }

    public boolean isUserTurn(User user) {
        return currentPlayer == user;
        // Implement this to check if it's `user`'s turn
    }

    public void createNPC() {
    }


    public void advanceTimeByOneHour() {
      timeAndDate.advanceHour();
      getCurrentPlayer().handleSpecialFoodsEffects();
      updateMachines();
//      for (NPC npc : this.getNpcs()) {
//          npc.updateRoutine(this);
//      }
    }

    public void updateMachines() {  //use this method every hour
        for (User user : players) {
            Farm farm = getMap().getFarmByOwner(user);
            System.out.println("farm :" + farm);
            House house = getMap().getFarmByOwner(user).getHouse();
            for (Machine machine : house.getMachines()) {
                if (machine.getActivated()) {
                    machine.setHoursLeft(machine.getHoursLeft() - 1);
                    if (machine.getHoursLeft() <= 0) {
                        machine.setActivated(false);
                        machine.setReady(true);
                        machine.setMaxProcessTime(0);
                    }
                }
            }
        }
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
            new NPCMission("Sebastian mission 1", Map.of("Iron Bar", 50), Map.of("Diamond", 2)),
            new NPCMission("Sebastian mission 2", Map.of("Pumpkin Pie",  1), Map.of("Gold Coin", 5000)),
            new NPCMission("Sebastian mission 3", Map.of("Rock", 150), Map.of("Quartz", 50))
        )), 30, new Point(NPCtype.Sebastian.getHomeLocation().x, NPCtype.Sebastian.getHomeLocation().y));
        npcs.add(sebastian);
        Tile sebastianHomeTile = map.getTile(sebastian.getNpcName().getHomeLocation().x, sebastian.getNpcName().getHomeLocation().y);
        if (sebastianHomeTile != null) {
            sebastian.setCurrentTile(sebastianHomeTile);
            sebastianHomeTile.setContainedNPC(sebastian);
        }

        NPC abigail = new NPC(NPCtype.Abigail, players, new ArrayList<>(Arrays.asList(
            new NPCMission("Abigail mission 1", Map.of("Gold Bar", 1), Map.of("Friendship Level", 1)),
            new NPCMission("Abigail mission 2", Map.of("Pumpkin", 1), Map.of("Gold Coin", 500)),
            new NPCMission("Abigail mission 3", Map.of("Wheat", 50), Map.of("Sprinkler", 1))
        )), 60,new Point(NPCtype.Abigail.getHomeLocation().x, NPCtype.Abigail.getHomeLocation().y));
        npcs.add(abigail);
        Tile abigailHomeTile = map.getTile(abigail.getNpcName().getHomeLocation().x, abigail.getNpcName().getHomeLocation().y);
        if (abigailHomeTile != null) {
            abigail.setCurrentTile(abigailHomeTile);
            abigailHomeTile.setContainedNPC(abigail);
        }

        NPC harvey = new NPC(NPCtype.Harvey, players, new ArrayList<>(Arrays.asList(
            new NPCMission("Harvey mission 1", Map.of("Strawberry", 12), Map.of("Gold Coin", 750)),
            new NPCMission("Harvey mission 2", Map.of("Salmon", 1), Map.of("Friendship Level", 1)),
            new NPCMission("Harvey mission 3", Map.of("Wine", 1), Map.of("Salad", 5))
        )), 40, new Point(NPCtype.Harvey.getHomeLocation().x, NPCtype.Harvey.getHomeLocation().y));
        npcs.add(harvey);
        Tile harveyHomeTile = map.getTile(harvey.getNpcName().getHomeLocation().x, harvey.getNpcName().getHomeLocation().y);
        if (harveyHomeTile != null) {
            harvey.setCurrentTile(harveyHomeTile);
            harveyHomeTile.setContainedNPC(harvey);
        }

        NPC leah = new NPC(NPCtype.Leah, players, new ArrayList<>(Arrays.asList(
            new NPCMission("Leah mission 1", Map.of("Wood", 10), Map.of("Gold Coin", 500)),
            new NPCMission("Leah mission 2", Map.of("Salmon", 1), Map.of("Salmon Dinner Recipe", 1)),
            new NPCMission("Leah mission 3", Map.of("Wood", 200), Map.of("Deluxe Scarecrow", 3))
        )), 90, new Point(NPCtype.Leah.getHomeLocation().x, NPCtype.Leah.getHomeLocation().y));
        npcs.add(leah);
        Tile leahHomeTile = map.getTile(leah.getNpcName().getHomeLocation().x, leah.getNpcName().getHomeLocation().y);
        if (leahHomeTile != null) {
            leah.setCurrentTile(leahHomeTile);
            leahHomeTile.setContainedNPC(leah);
        }

        NPC robin = new NPC(NPCtype.Robin, players, new ArrayList<>(Arrays.asList(
            new NPCMission("Robin mission 1", Map.of("Wood", 80), Map.of("Gold Coin", 1000)),
            new NPCMission("Robin mission 2", Map.of("Iron Bar", 10), Map.of("Bee House", 3)),
            new NPCMission("Robin mission 3", Map.of("Wood", 1000), Map.of("Gold Coin", 25000))
        )), 120, new Point(NPCtype.Robin.getHomeLocation().x, NPCtype.Abigail.getHomeLocation().y));
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

    public void reloadExtraData() {
        for (Tile[] row : map.getMap()) {
            for (Tile tile : row) {
                Animal animal = tile.getContainedAnimal();
                if (animal != null) {
                    animal.reloadAfterLoad(tile);
                }
                NPC npc = tile.getContainedNPC();
                if (npc != null) {
                    npc.reloadAfterLoad(tile);
                }
            }
        }
    }

    public Map<String, List<NPCMission>> getPlayerAddedMissions() {
        return playerAddedMissions;
    }

    public String getNetworkId() {
        return NetworkId;
    }

    public void setNetworkId(String networkId) {
        NetworkId = networkId;
    }

    public void markPlayerSelectedMap(String username) {
        mapSelectionStatus.put(username, true);
    }

    public boolean haveAllPlayersSelectedMap() {
        for (User user : players) {
            if (!mapSelectionStatus.getOrDefault(user.getUsername(), false)) {
                return false;
            }
        }
        return true;
    }

    public void resetMapSelectionStatus() {
        mapSelectionStatus.clear();
        for (User user : players) {
            mapSelectionStatus.put(user.getUsername(), false);
        }
    }

    public void markPlayerLoadingGame(String username) {
        loadStatus.put(username, true);
    }

    public void unmarkPlayerLoadingGame(String username) {
        loadStatus.put(username, false);
    }

    public boolean haveAllPlayersLoadedGame() {
        for (User user : players) {
            if (!loadStatus.getOrDefault(user.getUsername(), false)) {
                return false;
            }
        }
        return true;
    }

    public void resetLoadGamesStatus() {
        loadStatus.clear();
        for (User user : players) {
            loadStatus.put(user.getUsername(), false);
        }
    }
    public void setUserByUsername(User player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(player.getUsername())) {
                players.set(i, player);
                return;
            }
        }
    }

    public static String getTradeSessionKey(String user1, String user2) {
        if (user1.compareTo(user2) > 0) {
            String temp = user1;
            user1 = user2;
            user2 = temp;
        }
        return user1 + "_" + user2;
    }

    public Map<String, Trade> getActiveTrades() {
        return activeTrades;
    }

    public void endTradeSession(String user1, String user2) {
        getActiveTrades().remove(getTradeSessionKey(user1, user2));
    }

    private void generateScenery() {
        Random random = new Random(this.NetworkId.hashCode()); // Seed with game ID for consistency
        if (map != null) {
            int treesToPlace = 300;
            int bushesToPlace = 200;
            int sceneryCount = 0;

            while (sceneryCount < treesToPlace + bushesToPlace) {
                int x = random.nextInt(map.getWidth());
                int y = random.nextInt(map.getHeight());

                // Check if the point is within any farm or on NPCLAND
                if (map.isInsideAnyFarm(x, y) != null || map.getMap()[y][x].getType() == TileType.NPCLAND) {
                    continue; // Skip this point and try another
                }

                if (sceneryCount < treesToPlace) {
                    treePoints.add(new Point(x, y));
                } else {
                    bushPoints.add(new Point(x, y));
                }
                sceneryCount++;
            }
        }
    }

    public List<Point> getTreePoints() {
        return treePoints;
    }

    public List<Point> getBushPoints() {
        return bushPoints;
    }

}
