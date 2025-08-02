package io.github.stardew.mini.Model.NPCManagement;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.ConfigLoader;
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

    private Point currentPoint;
    @JsonIgnore
    private Tile currentTile;

    private Queue<Point> pathToTarget = new LinkedList<>();
    private Point movingFrom = null;
    private Point movingTo = null;

    private float moveSpeed = 1f;

    public NPC(NPCtype npcName, ArrayList<User> users,
               ArrayList<NPCMission> missions, int daysLeftToUnlockThirdMission
                , Point currentPoint) {
        this.npcName = npcName;
        this.friendshipLevels = new HashMap<>();
        this.friendshipPoints = new HashMap<>();
        this.talkedToNPCToday = new HashMap<>();
        this.gaveGiftToNPCToday = new HashMap<>();
        this.currentPoint = currentPoint;
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
                User user = currentGame.getPlayerByUsername(username);
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
        String OPENROUTER_API_KEY = ConfigLoader.getApiKey();
        String LLM_MODEL = "qwen/qwen3-235b-a22b-07-25:free";
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

    public Point currentPointGetter() {
        return this.currentPoint;
    }

    public Tile currentTileGetter() {
        return this.currentTile;
    }

    public void updateRoutine(Game currentGame) {
        MapOfGame map = currentGame.getMap();
        int currentHour = currentGame.getTimeAndDate().getHour();

        Point targetLocation = null;

        if (currentHour >= 6 && currentHour < 10) {
            targetLocation = npcName.getHomeLocation();
        }
        else if (currentHour >= 10 && currentHour < 17) {
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

        if (!isMoving() && (currentPoint == null || !currentPoint.equals(targetLocation))) {
            List<Point> path = findShortestPath(currentPoint, targetLocation, map, 1000);
            if (!path.isEmpty()) {
                setPathToTarget(path);
            }
            else {
                System.out.println("No path found for " + targetLocation);
            }
        }
        updateMovement(0.018f);
    }

    public boolean isMoving() {
        return movingTo != null;
    }

    public void startMove(Point from, Point to) {
        this.movingFrom = from;
        this.movingTo = to;
    }

    public void updateMovement(float delta) {
        if (MainApp.getInstance().getCurrentGame() == null) {
            System.out.println("currrentgame is null");
            return;
        }
        if (movingTo != null) {
            currentPoint = movingTo;
            movingFrom = null;
            movingTo = null;

            if (!pathToTarget.isEmpty()) {
                startMove(currentPoint, pathToTarget.poll());
            }
            else {
                Map<String, Object> params = new HashMap<>();
                params.put("npcName", this.getName());
                params.put("currentPoint", currentPoint);
                params.put("movingTo", movingTo);
                params.put("movingFrom", movingFrom);
                MainApp.getInstance().getNetworkClient().sendPost(
                    MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController",
                    "updateNpcPosition",
                    params,
                    MainApp.getInstance().getLoggedInUser().getUsername()
                );
                this.currentTile.setContainedNPC(null);
                this.currentTile = MainApp.getInstance().getCurrentGame().getMap().getTile(currentPoint.x, currentPoint.y);
                currentTile.setContainedNPC(this);
            }
        }
    }

    public void setPathToTarget(List<Point> path) {
        this.pathToTarget.clear();
        this.pathToTarget.addAll(path);
        if (!this.pathToTarget.isEmpty()) {
            startMove(currentPoint, this.pathToTarget.poll());
        }
    }

    public Point getMovingFrom() {
        return movingFrom;
    }

    public Point getMovingTo() {
        return movingTo;
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    private List<Point> findShortestPath(Point start, Point goal, MapOfGame map, int maxSteps) {
        if (start == null || goal == null || start.equals(goal)) {
            return new ArrayList<>();
        }

        List<Point> path = new ArrayList<>();
        int currentX = start.x;
        int currentY = start.y;

        // Move horizontally first
        while (currentX != goal.x) {
            if (path.size() >= maxSteps) return new ArrayList<>(); // Path is too long
            currentX += (goal.x > currentX) ? 1 : -1;
            path.add(new Point(currentX, currentY));
        }

        // Then move vertically
        while (currentY != goal.y) {
            if (path.size() >= maxSteps) return new ArrayList<>(); // Path is too long
            currentY += (goal.y > currentY) ? 1 : -1;
            path.add(new Point(currentX, currentY));
        }

        return path;
    }

    private List<Point> getWalkableNeighbors(Point tile, MapOfGame map) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int nx = tile.x + dir[0];
            int ny = tile.y + dir[1];
            Tile neighbor = map.getTile(nx, ny);
            if (neighbor != null && neighbor.getisWalkable()) {
                if (neighbor.getContainedNPC() == null || neighbor.getContainedNPC().equals(this)) {
                    neighbors.add(new Point(nx, ny));
                }
            }
        }
        return neighbors;
    }

    public void reloadAfterLoad(Tile tile) {
        this.currentPoint = new Point(tile.getX(), tile.getY());
        this.currentTile = tile;
        //this.pathToTarget = new LinkedList<>();

        if (this.currentPoint != null && tile.getContainedNPC() == null) {
            tile.setContainedNPC(this);
        }
    }


    public void setMovingTo(Point movingTo) {
        this.movingTo = movingTo;
    }

    public void setMovingFrom(Point movingFrom) {
        this.movingFrom = movingFrom;
    }
}
