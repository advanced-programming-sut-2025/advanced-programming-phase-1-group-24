package io.github.stardew.mini.Model.NPCManagement;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Reccepies.FoodRecipe;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.User;

import java.util.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Point;

import com.badlogic.gdx.math.MathUtils;
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NPC {
    private NPCtype npcName;
    private Map <String, Integer> friendshipLevels;
    private Map <String, Integer> friendshipPoints;
    private Map <String, Boolean> talkedToNPCToday;
    private Map <String, Boolean> gaveGiftToNPCToday;
    private ArrayList<NPCMission> missions;
    private Map <String, ArrayList<NPCMission>> unlockedMissions;
    private int daysLeftToUnlockThirdMission;

    @JsonIgnore
    private Tile currentTile;

    private float movementCooldown = 0f;

    @JsonIgnore
    private Queue<Tile> pathToTarget = new LinkedList<>();

    @JsonIgnore
    private Tile movingFrom = null;

    @JsonIgnore
    private Tile movingTo = null;

    private float moveProgress = 0f;
    private float moveSpeed = 0.7f;

    public NPC(NPCtype npcName, ArrayList<User> users,
               ArrayList<NPCMission> missions, int daysLeftToUnlockThirdMission) {
        this.npcName = npcName;
        this.friendshipLevels = new HashMap<>();
        this.friendshipPoints = new HashMap<>();
        this.talkedToNPCToday = new HashMap<>();
        this.gaveGiftToNPCToday = new HashMap<>();
        this.missions = new ArrayList<>(missions);
        this.unlockedMissions = new HashMap<>();
        for (User user : users) {
            friendshipLevels.put(user.getUsername(), 0);
            friendshipPoints.put(user.getUsername(), 0);
            talkedToNPCToday.put(user.getUsername(), false);
            gaveGiftToNPCToday.put(user.getUsername(), false);
            unlockedMissions.put(user.getUsername(), new ArrayList<>());
            unlockedMissions.get(user.getUsername()).add(missions.get(0));
        }
        this.daysLeftToUnlockThirdMission = daysLeftToUnlockThirdMission;
    }

    public NPC() {
        this.pathToTarget = new LinkedList<>();
    }

    public String getName() {
        return npcName.getName();
    }

    public NPCtype getNpcName() {
        return npcName;
    }


    public Map<String, Integer> getFriendshipLevels() {
        return friendshipLevels;
    }

    public Map<String, Boolean> getGaveGiftToNPCToday() {
        return gaveGiftToNPCToday;
    }

    public Map<String, Boolean> getTalkedToNPCToday() {
        return talkedToNPCToday;
    }

    public Map<String, Integer> getFriendshipPoints() {
        return friendshipPoints;
    }

    public ArrayList<NPCMission> getMissions() {
        return missions;
    }

    public Map<String, ArrayList<NPCMission>> getUnlockedMissions() {

        return unlockedMissions;
    }

    public int getDaysLeftToUnlockThirdMission() {
        return daysLeftToUnlockThirdMission;
    }

    public void setDaysLeftToUnlockThirdMission(int daysLeftToUnlockThirdMission) {
        this.daysLeftToUnlockThirdMission = daysLeftToUnlockThirdMission;
    }

    public Result talkToNPC (WeatherType currentWeather, User currentPlayer){
//        for (Dialog dialog : npcName.getDialogs()) {
//            if (currentWeather.equals(dialog.getWeatherType())
//                    && friendshipLevels.get(currentPlayer.getUsername()) == dialog.getRequiredFriendshipLevel()) {
//                if (!talkedToNPCToday.get(currentPlayer.getUsername())) {
//                    friendshipPoints.merge(currentPlayer.getUsername(), 20, Integer::sum);
//                    this.updateFriendshipLevel(currentPlayer);
//                    talkedToNPCToday.put(currentPlayer.getUsername(), true);
//                }
//                return dialog.useDialog();
//            }
//        }
//        return new Result(false,"No dialog available");
        return generateDialogueFromLLM(currentWeather, currentPlayer);
    }
    public Result doMission(int missionIndex, User currentPlayer) {

        if (missionIndex > unlockedMissions.size()) { return new Result(false, "False index."); }
        missionIndex--;
        if (missionIndex < 0) { return new Result(false, "False index."); }

        NPCMission mission = unlockedMissions.get(currentPlayer.getUsername()).get(missionIndex);
        if (mission.getAlreadyDone()) return new Result(false,"This mission is already done.");

        for (String itemName : mission.getRequiredItems().keySet()) {
            if (!currentPlayer.getBackpack().hasItem(itemName, mission.getRequiredItems().get(itemName)))
                return new Result(false, "You dont have the required items.");
        }

        for (String itemName : mission.getRequiredItems().keySet()) {
            currentPlayer.getBackpack().grabItem(itemName, mission.getRequiredItems().get(itemName));
        }
        int howManyItems = 1;
        if (friendshipLevels.get(currentPlayer.getUsername()) >= 2)  howManyItems = 2;
        for (String itemName : mission.getPrizeItems().keySet()) {
            if (itemName.equals("Gold Coin")) {
                currentPlayer.addMoney( mission.getPrizeItems().get(itemName) * howManyItems);
            }
            else if (itemName.equals("Friendship Level")) {
                friendshipLevels.put(currentPlayer.getUsername(),friendshipLevels.get(currentPlayer.getUsername()) + 1);
            }
            else if (itemName.equals("Salmon Dinner Recipe")) {
                currentPlayer.getCookingRecepies().add(FoodRecipe.SalmonDinner);
            }
            else {
                Item item = Item.getRandomItem(itemName);
                currentPlayer.getBackpack().addItem(item, mission.getPrizeItems().get(itemName) * howManyItems);
            }
        }
        mission.setAlreadyDone(true);
        return new Result(true, "Mission was completed successfully.");
    }

    public Result giveGift(String itemName, User currentPlayer){
        if (gaveGiftToNPCToday.get(currentPlayer.getUsername())) {
            return new Result(false,"You already gave a gift today.");
        }
        for (String favoriteItem : npcName.getFavoriteItems()) {
            if (favoriteItem.equals(itemName)) {
                friendshipPoints.merge(currentPlayer.getUsername(), 200, Integer::sum);
                this.updateFriendshipLevel(currentPlayer);
                gaveGiftToNPCToday.put(currentPlayer.getUsername(), true);
                return new Result(true,"I love your gift!!");
            }
        }
        friendshipPoints.merge(currentPlayer.getUsername(), 50, Integer::sum);
        this.updateFriendshipLevel(currentPlayer);
        gaveGiftToNPCToday.put(currentPlayer.getUsername(), true);
        return new Result(true,"Thank you!");
    }

    public static void endOfDay(Game currentGame) {
        for (NPC npc : currentGame.getNpcs()) {
            if (npc.getDaysLeftToUnlockThirdMission() > 0)
                npc.setDaysLeftToUnlockThirdMission(npc.getDaysLeftToUnlockThirdMission() - 1);
            for (String username : npc.getTalkedToNPCToday().keySet()) {
                User user = MainApp.getInstance().getCurrentGame().getPlayerByUsername(username);
                npc.getTalkedToNPCToday().put(user.getUsername(), false);
                npc.getGaveGiftToNPCToday().put(user.getUsername(), false);
                if (npc.getFriendshipLevels().get(user.getUsername()) == 3) {
                    int random = (int) (Math.random() + 0.5);
                    String itemName = npc.getNpcName().getRandomGifts().get(random);
                    Item item = Item.getRandomItem(itemName);
                    user.getBackpack().addItem(item, 1);
                    //might add a message to show that the user received a gift later
                }
                if (npc.getDaysLeftToUnlockThirdMission() == 0) {
                    npc.getUnlockedMissions().get(user.getUsername()).add(npc.getMissions().get(2));
                    npc.setDaysLeftToUnlockThirdMission(-1);
                    //might add a message to show that a new message has been unlocked
                }
            }
        }
    }

    public void updateFriendshipLevel(User currentPlayer) {
        for (String username : friendshipPoints.keySet()) {
            User user = MainApp.getInstance().getCurrentGame().getPlayerByUsername(username);
            if (friendshipPoints.get(user.getUsername()) > 799) friendshipPoints.put(user.getUsername(), 799);
            int previousFriendshipLevel = friendshipLevels.get(user.getUsername());
            int newFriendshipLevel = (int)Math.floor(friendshipPoints.get(user.getUsername()) / 200);
            if (newFriendshipLevel != previousFriendshipLevel) {
                friendshipLevels.put(user.getUsername(), newFriendshipLevel);
                if (newFriendshipLevel == 1) unlockedMissions.get(currentPlayer.getUsername()).add(missions.get(1));
            }
        }
    }

    public Boolean checkIfIsNearNPC(Tile userCurrentTile) {
        int userX = userCurrentTile.getX();
        int userY = userCurrentTile.getY();
        int[] xDirections = {1,1,0,-1,-1,-1,0,1};
        int[] yDirections = {0,-1,-1,-1,0,1,1,1};
        for (int i = 0; i < 8; i++) {
            if (this.equals(MainApp.getInstance().getCurrentGame().getMap().getTile(userX + xDirections[i], userY + yDirections[i]).getContainedNPC())) {
                return true;
            }
        }
        return false;
    }

    private Result generateDialogueFromLLM(WeatherType currentWeather, User currentPlayer) {
        String OPENROUTER_API_KEY = "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";
        String LLM_MODEL = "qwen/qwen3-coder:free";
        String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String playerName = currentPlayer.getUsername();
        int friendshipLevel = friendshipLevels.get(playerName);
        String npcNameStr = npcName.getName();
        String currentSeason = MainApp.getInstance().getCurrentGame().getTimeAndDate().getSeason().name();
        int currentDay = MainApp.getInstance().getCurrentGame().getTimeAndDate().getDay();

        String userPrompt = String.format(
            "You are %s from Stardew Valley. The player %s is talking to you. " +
                "It's currently %s season, day %d, and the weather is %s. " +
                "Your friendship level with %s is %d. " +
                "Say something natural for this context. Keep it concise (1-2 sentences). " +
                "Consider your personality from Stardew Valley. Do not include your name in the response.",
            npcNameStr, playerName, currentSeason, currentDay, currentWeather.name(), playerName, friendshipLevel
        );

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", LLM_MODEL);

        JsonArray messages = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a Stardew Valley NPC. Respond briefly.");
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userPrompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 50);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OPENROUTER_API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + OPENROUTER_API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
                String generatedText = responseJson
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

                if (!talkedToNPCToday.get(currentPlayer.getUsername())) {
                    friendshipPoints.merge(currentPlayer.getUsername(), 20, Integer::sum);
                    this.updateFriendshipLevel(currentPlayer);
                    talkedToNPCToday.put(currentPlayer.getUsername(), true);
                }
                return new Result(true, generatedText);
            } else {
                System.err.println("Error calling LLM API: " + response.statusCode() + " - " + response.body());
                return new Result(false, "LLM failed to generate dialogue: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Exception during LLM API call: " + e.getMessage());
            return new Result(false, "Could not connect to dialogue AI.");
        }
    }

    public Tile currentTileGetter() {
        return this.currentTile;
    }

    public void updateRoutine(Game currentGame) {
        MapOfGame map = currentGame.getMap();
        int currentHour = currentGame.getTimeAndDate().getHour();
        int currentDayOfWeek = currentGame.getTimeAndDate().getDayOfWeek().ordinal(); // 0 for Sunday, 6 for Saturday

        Point targetLocation = null;

        if (currentHour >= 6 && currentHour < 9) {
            targetLocation = npcName.getHomeLocation();
        }
        else if (currentHour >= 9 && currentHour < 17) {
            targetLocation = npcName.getWorkLocation();
        }
        else if (currentHour >= 17 && currentHour < 22) {
            targetLocation = npcName.getSocialLocation();
        }
        else {
            targetLocation = npcName.getHomeLocation();
        }

        if (targetLocation == null) {
            return;
        }

        Tile destinationTile = map.getTile(targetLocation.x, targetLocation.y);

        if (!isMoving() && (currentTile == null || !currentTile.equals(destinationTile))) {
            List<Tile> path = findShortestPath(currentTile, destinationTile, map, 50);
            if (!path.isEmpty()) {
                setPathToTarget(path);
            }
        }
        updateMovement(0.0166f);
    }

    public boolean isMoving() {
        return movingTo != null;
    }

    public void startMove(Tile from, Tile to) {
        this.movingFrom = from;
        this.movingTo = to;
        this.moveProgress = 0f;
    }

    public void updateMovement(float delta) {
        if (movingTo != null) {
            moveProgress += moveSpeed * delta;
            if (moveProgress >= 1f) {
                moveProgress = 0f;
                if (currentTile != null) {
                    currentTile.setContainedNPC(null);
                }
                currentTile = movingTo;
                currentTile.setContainedNPC(this);
                movingFrom = null;
                movingTo = null;

                if (!pathToTarget.isEmpty()) {
                    startMove(currentTile, pathToTarget.poll());
                } else {
                    resetCooldown();
                }
            }
        }
    }

    public void setPathToTarget(List<Tile> path) {
        this.pathToTarget.clear();
        this.pathToTarget.addAll(path);
        if (!this.pathToTarget.isEmpty()) {
            startMove(currentTile, this.pathToTarget.poll());
        }
    }

    public Tile getMovingFrom() {
        return movingFrom;
    }

    public Tile getMovingTo() {
        return movingTo;
    }

    public float getMoveProgress() {
        return moveProgress;
    }

    public float getMovementCooldown() {
        return movementCooldown;
    }

    public void reduceCooldown(float delta) {
        movementCooldown -= delta;
    }

    public void resetCooldown() {
        movementCooldown = (3f + MathUtils.random(2f));
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    private List<Tile> findShortestPath(Tile start, Tile goal, MapOfGame map, int maxSteps) {
        if (start == null || goal == null || start.equals(goal)) return new ArrayList<>();

        Queue<Tile> queue = new LinkedList<>();
        Map<Tile, Tile> cameFrom = new HashMap<>();
        Set<Tile> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        cameFrom.put(start, null);

        int steps = 0;
        while (!queue.isEmpty() && steps <= maxSteps) {
            Tile current = queue.poll();

            if (current.equals(goal)) break;

            for (Tile neighbor : getWalkableNeighbors(current, map)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
            steps++;
        }

        List<Tile> path = new LinkedList<>();
        Tile step = goal;
        while (step != null && !step.equals(start)) {
            path.add(0, step);
            step = cameFrom.get(step);
        }

        if (path.size() > maxSteps || !path.contains(goal)) return new ArrayList<>();
        return path;
    }

    private List<Tile> getWalkableNeighbors(Tile tile, MapOfGame map) {
        List<Tile> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int nx = tile.getX() + dir[0];
            int ny = tile.getY() + dir[1];
            Tile neighbor = map.getTile(nx, ny);
            if (neighbor != null && neighbor.getisWalkable()) {
                if (neighbor.getContainedNPC() == null || neighbor.getContainedNPC().equals(this)) {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    public void reloadAfterLoad(Tile tile) {
        this.currentTile = tile;
        this.pathToTarget = new LinkedList<>();
        this.movingFrom = null;
        this.movingTo = null;
        this.moveProgress = 0f;
        this.movementCooldown = 0f;

        if (tile.getContainedNPC() == null || !tile.getContainedNPC().equals(this)) {
            tile.setContainedNPC(this);
        }
    }


}
