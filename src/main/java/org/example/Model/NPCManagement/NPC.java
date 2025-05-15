package org.example.Model.NPCManagement;

import org.example.Model.App;
import org.example.Model.Game;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Reccepies.FoodRecipe;
import org.example.Model.Result;
import org.example.Model.Things.Item;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NPC {
    private NPCtype npcName;
    //private Tile currentTile;
    private Map <String, Integer> friendshipLevels;
    private Map <String, Integer> friendshipPoints;
    private Map <String, Boolean> talkedToNPCToday;
    private Map <String, Boolean> gaveGiftToNPCToday;
    private ArrayList<NPCMission> missions;
    private Map <String, ArrayList<NPCMission>> unlockedMissions;
    private int daysLeftToUnlockThirdMission;

    public NPC(NPCtype npcName, ArrayList<User> users,
               ArrayList<NPCMission> missions, int daysLeftToUnlockThirdMission) {
        this.npcName = npcName;
        //this.currentTile = currentTile;
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

    public String getName() {
        return npcName.getName();
    }

    public NPCtype getNpcName() {
        return npcName;
    }

//    public Tile getCurrentTile() {
//        return currentTile;
//    }


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
        for (Dialog dialog : npcName.getDialogs()) {
            if (currentWeather.equals(dialog.getWeatherType())
                    && friendshipLevels.get(currentPlayer.getUsername()) == dialog.getRequiredFriendshipLevel()) {
                if (!talkedToNPCToday.get(currentPlayer.getUsername())) {
                    friendshipPoints.merge(currentPlayer.getUsername(), 20, Integer::sum);
                    this.updateFriendshipLevel(currentPlayer);
                    talkedToNPCToday.put(currentPlayer.getUsername(), true);
                }
                return dialog.useDialog();
            }
        }
        return new Result(false,"No dialog available");
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
        String message = "";
        if (friendshipLevels.get(currentPlayer.getUsername()) >= 2)  howManyItems = 2;
        for (String itemName : mission.getPrizeItems().keySet()) {
            if (itemName.equals("Gold Coin")) {
                currentPlayer.addMoney( mission.getPrizeItems().get(itemName) * howManyItems);
                message = "Your current money is " + currentPlayer.getMoney() + ".";
            }
            else if (itemName.equals("Friendship Level")) {
                friendshipLevels.put(currentPlayer.getUsername(),friendshipLevels.get(currentPlayer.getUsername()) + 1);
                message = "Your new Friendship level is " + friendshipLevels.get(currentPlayer.getUsername()) + ".";
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
        return new Result(true, "Mission was completed successfully." + message);
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
                User user = App.getInstance().getCurrentGame().getPlayerByUsername(username);
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
            User user = App.getInstance().getCurrentGame().getPlayerByUsername(username);
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
            if (this.equals(App.getInstance().getCurrentGame().getMap().getTile(userX + xDirections[i], userY + yDirections[i]).getContainedNPC())) {
                return true;
            }
        }
        return false;
    }


}
