package org.example.Model.NPCManagement;

import org.example.Model.Game;
import org.example.Model.MapManagement.Tile;
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
    private Tile currentTile;
    private Map <User, Integer> friendshipLevels;
    private Map <User, Integer> friendshipPoints;
    private Map <User, Boolean> talkedToNPCToday;
    private Map <User, Boolean> gaveGiftToNPCToday;
    private ArrayList<NPCMission> missions;
    private Map <User, ArrayList<NPCMission>> unlockedMissions;
    private int daysLeftToUnlockThirdMission;

    public NPC(NPCtype npcName, Tile currentTile, ArrayList<User> users,
               ArrayList<NPCMission> missions, int daysLeftToUnlockThirdMission) {
        this.npcName = npcName;
        this.currentTile = currentTile;
        this.friendshipLevels = new HashMap<>();
        this.friendshipPoints = new HashMap<>();
        this.talkedToNPCToday = new HashMap<>();
        this.gaveGiftToNPCToday = new HashMap<>();
        this.missions = new ArrayList<>(missions);
        this.unlockedMissions = new HashMap<>();
        for (User user : users) {
            friendshipLevels.put(user, 0);
            friendshipPoints.put(user, 0);
            talkedToNPCToday.put(user, false);
            gaveGiftToNPCToday.put(user, false);
            unlockedMissions.put(user, new ArrayList<>());
            //unlockedMissions.get(user).add(missions.getFirst());
        }
        this.daysLeftToUnlockThirdMission = daysLeftToUnlockThirdMission;
    }

    public String getName() {
        return npcName.getName();
    }

    public NPCtype getNpcName() {
        return npcName;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Map<User, Integer> getFriendshipLevels() {
        return friendshipLevels;
    }

    public Map<User, Integer> getFriendshipPoints() {
        return friendshipPoints;
    }

    public Map<User, Boolean> getTalkedToNPCToday() {
        return talkedToNPCToday;
    }

    public Map<User, Boolean> getGaveGiftToNPCToday() {
        return gaveGiftToNPCToday;
    }

    public ArrayList<NPCMission> getMissions() {
        return missions;
    }

    public Map<User, ArrayList<NPCMission>> getUnlockedMissions() {
        return unlockedMissions;
    }

    public int getDaysLeftToUnlockThirdMission() {
        return daysLeftToUnlockThirdMission;
    }

    public Result talkToNPC (WeatherType currentWeather, User currentPlayer){
        for (Dialog dialog : npcName.getDialogs()) {
            if (currentWeather.equals(dialog.getWeatherType())
                    && friendshipLevels.get(currentPlayer) == dialog.getRequiredFriendshipLevel()) {
                if (!talkedToNPCToday.get(currentPlayer)) {
                    friendshipPoints.merge(currentPlayer, 20, Integer::sum);
                    this.updateFriendshipLevel(currentPlayer);
                    talkedToNPCToday.put(currentPlayer, true);
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

        NPCMission mission = unlockedMissions.get(currentPlayer).get(missionIndex);
        if (mission.getAlreadyDone()) return new Result(false,"This mission is already done.");

        for (String itemName : mission.getRequiredItems().keySet()) {
            if (!currentPlayer.getBackpack().hasItem(itemName, mission.getRequiredItems().get(itemName)))
                return new Result(false, "You dont have the required items.");
        }

        for (String itemName : mission.getRequiredItems().keySet()) {
            currentPlayer.getBackpack().grabItem(itemName, mission.getRequiredItems().get(itemName));
        }
        int howManyItems = 1;
        if (friendshipLevels.get(currentPlayer) >= 2)  howManyItems = 2;
        for (String itemName : mission.getPrizeItems().keySet()) {
            if (itemName.equals("Gold Coin")) {
                currentPlayer.setMoney(currentPlayer.getMoney() + mission.getPrizeItems().get(itemName) * howManyItems);
            }
            else if (itemName.equals("Friendship Level")) {
                friendshipLevels.put(currentPlayer,friendshipLevels.get(currentPlayer) + 1);
            }
            else {
                //Item item = Item.getRandomItem(itemName);
                //currentPlayer.getBackpack().addItem(item, mission.getPrizeItems().get(itemName) * howManyItems);
            }
        }
        mission.setAlreadyDone(true);
        return new Result(true, "Mission was completed successfully.");
    }

    public Result giveGift(String itemName, User currentPlayer){
        if (gaveGiftToNPCToday.get(currentPlayer)) {
            return new Result(false,"You already gave a gift today.");
        }
        for (String favoriteItem : npcName.getFavoriteItems()) {
            if (favoriteItem.equals(itemName)) {
                friendshipPoints.merge(currentPlayer, 200, Integer::sum);
                this.updateFriendshipLevel(currentPlayer);
                return new Result(true,"I love your gift!!");
            }
        }
        friendshipPoints.merge(currentPlayer, 50, Integer::sum);
        this.updateFriendshipLevel(currentPlayer);
        return new Result(true,"Thank you!");
    }

    public void endOfDay() {
        if (daysLeftToUnlockThirdMission > 0) this.daysLeftToUnlockThirdMission--;
        for (User user : talkedToNPCToday.keySet()) {
            talkedToNPCToday.put(user, false);
            gaveGiftToNPCToday.put(user, false);
            if (friendshipLevels.get(user) == 3) {
                int random = (int) (Math.random() + 0.5);
                String itemName = npcName.getRandomGifts().get(random);
                Item item = Item.getRandomItem(itemName);
                user.getBackpack().addItem(item,1);
                //might add a message to show that the user received a gift later
            }
            if (daysLeftToUnlockThirdMission == 0) {
                unlockedMissions.get(user).add(missions.get(2));
                //might add a message to show that a new message has been unlocked
            }
        }
    }

    public void updateFriendshipLevel(User currentPlayer) {
        for (User user : friendshipPoints.keySet()) {
            if (friendshipPoints.get(user) > 799) friendshipPoints.put(user, 799);
            int previousFriendshipLevel = friendshipLevels.get(user);
            int newFriendshipLevel = (int)Math.floor(friendshipPoints.get(user) / 200);
            if (newFriendshipLevel != previousFriendshipLevel) {
                friendshipLevels.put(user, newFriendshipLevel);
                if (newFriendshipLevel == 1) unlockedMissions.get(currentPlayer).add(missions.get(1));
            }
        }
    }

    public Boolean checkIfIsNearNPC(Tile userCurrentTile) {
        int userX = userCurrentTile.getX();
        int userY = userCurrentTile.getY();
        int NPCX = currentTile.getX();
        int NPCY = currentTile.getY();
        if ( (userX == NPCX || userX + 1 == NPCX || userX - 1 == NPCX) &&
                (userY == NPCY || userY + 1 == NPCY || userY - 1 == NPCY) ) {
            return true;
        }
        else return false;
    }


}