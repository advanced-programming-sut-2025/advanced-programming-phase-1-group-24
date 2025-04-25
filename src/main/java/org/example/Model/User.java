package org.example.Model;


import org.example.Model.MapManagement.Tile;
import org.example.Model.Reccepies.Craft;
import org.example.Model.Reccepies.Food;
import org.example.Model.Things.Backpack;
import org.example.Model.Things.Item;
import org.example.Model.Tools.Tool;
import org.example.Model.Tools.ToolType;

import java.util.ArrayList;
import java.util.Map;

public class User {
    String username;
    String password;
    String nickname;
    String email;
    boolean gender;
    int playedGames;
    int maxMoneyInGames;
    private String securityQuestion;
    private String securityAnswer;

    //player
    private int maxEnergy = 200;
    private int maxEnergyTurn = 50;
    private int energy = maxEnergy;
    private int currentTurnEnergy = maxEnergyTurn;
    /* a variable for hours left for special energy Max (eg.coffee)then after each turn if this variable
    is more than one we minus one and then it when  reaches 0 we turn back the max energy to normal
     */
    private int money = 0;
    private boolean fainted = false;
    Map<Skill, Integer> skills;
    Tile currentTile;
    ToolType currentTool;
    ArrayList<Craft> craftingRecepies;
    ArrayList<Food> cookingRecepies;
    Map<User, Map<ArrayList<Item>, ArrayList<Item>>> tradeHistory;
    Map<User, FriendshipLevels> friends;
    Backpack backpack;
    Tool trashCan;


    public User(String username, String password, String nickname, String email, boolean gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
    }

    public int getCurrentTurnEnergy() {
        return currentTurnEnergy;
    }

    public void setCurrentTurnEnergy(int currentTurnEnergy) {
        this.currentTurnEnergy = currentTurnEnergy;
    }

    public void resetTurnEnergy() {
        this.currentTurnEnergy = maxEnergyTurn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public int getEnergy() {
        return energy;
    }

    public ArrayList<Food> getCookingRecepies() {
        return cookingRecepies;
    }

    public Map<User, Map<ArrayList<Item>, ArrayList<Item>>> getTradeHistory() {
        return tradeHistory;
    }

    public Map<User, FriendshipLevels> getFriends() {
        return friends;
    }

    //    public Shop getCurrentShop() {
//        return currentShop;
//    }
    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public ToolType getCurrentTool() {
        return currentTool;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public int getMaxMoneyInGames() {
        return maxMoneyInGames;
    }

    public int getMoney() {
        return money;
    }

    public void setMaxMoneyInGames(int maxMoneyInGames) {
        this.maxMoneyInGames = maxMoneyInGames;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void faint() {
    }

    public boolean hasFainted() {
        return fainted;
    }

    public void setFainted(boolean fainted) {
        this.fainted = fainted;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void trade() {
    }

    //always call this function before any task that consumes energy if returns false cant do the task
    public boolean tryConsumeEnergy(int energyRequired) {
        if (currentTurnEnergy < energyRequired || energy < energyRequired) {
            System.out.println("not enough energy!");
            return false;
        }
        currentTurnEnergy -= energyRequired;
        energy -= energyRequired;
        return true;
    }

    public void reduceEnergy(int amount) {
        this.currentTurnEnergy -= amount;
        this.energy -= amount;
        if (this.energy <= 0 || this.currentTurnEnergy <= 0) {
            this.energy = 0;
            this.currentTurnEnergy = 0;
            System.out.println("not enough energy! You faiented!");
            this.fainted = true;
            faint();
        }
    }

    public void updateGameFields() {
        this.playedGames += 1;
        this.energy = maxEnergy;
        this.money = 0;
        this.currentTurnEnergy = maxEnergyTurn;

        this.currentTile = null; // or a default starting tile
        this.currentTool = null;
        this.backpack = new Backpack(); // assuming it starts empty

        if (this.skills != null) {
            this.skills.clear(); // reset skills
        }

        if (this.craftingRecepies != null) {
            this.craftingRecepies.clear();
        }

        if (this.cookingRecepies != null) {
            this.cookingRecepies.clear();
        }

        if (this.tradeHistory != null) {
            this.tradeHistory.clear();
        }

        this.trashCan = null;
    }


    public void updateMaxMoney() {
        if (money > maxMoneyInGames) {
            maxMoneyInGames = money;
        }
    }

    public void resetEnergyForNewDay() {
        if (fainted) {
            this.energy = (int) (maxEnergy * 0.75);
            this.fainted = false;
            //System.out.println(username + " woke up with 75% energy at the same location.");
        } else {
            this.energy = maxEnergy;
            //System.out.println(username + " energy reset to full for the new day.");
        }
    }


}
