package org.example.Model;


import org.example.Model.MapManagement.Tile;
import org.example.Model.Reccepies.Craft;
import org.example.Model.Reccepies.Food;
import org.example.Model.Things.Backpack;
import org.example.Model.Things.Item;
import org.example.Model.Tools.Tool;
import org.example.Model.Tools.ToolType;

import java.util.ArrayList;
import java.util.HashMap;
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
    /* a variable for hours left for special energy Max (e.g.coffee)then after each turn if this variable
    is more than one we minus one and then it when  reaches 0 we turn back the max energy to normal
     */
    private int money = 0;
    private boolean fainted = false;
    Map<Skill, Integer> skillsLevel;
    private Map<Skill, Integer> skillExperience;
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
        this.skillsLevel = new HashMap<>();
        for (Skill skill : Skill.values()) {
            skillsLevel.put(skill, 0);
        }
        this.skillExperience = new HashMap<>();
        for (Skill skill : Skill.values()) {
            skillExperience.put(skill, 0);
        }
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

    //always call this function before any task that consumes energy if it returns false cant do the task
    public boolean tryConsumeEnergy(int energyRequired) {
        if (currentTurnEnergy < energyRequired || energy < energyRequired) {
            System.out.println("not enough energy!");
            return false;
        }
        currentTurnEnergy -= energyRequired;
        energy -= energyRequired;
        handleFainting();
        return true;
    }

    public void reduceEnergy(int amount) {
        this.currentTurnEnergy -= amount;
        this.energy -= amount;
        handleFainting();
    }

    public void handleFainting() {
        if (this.energy <= 0 || this.currentTurnEnergy <= 0) {
            this.energy = 0;
            this.currentTurnEnergy = 0;
            System.out.println("not enough energy! You faiented!");
            this.fainted = true;
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

        if (this.skillsLevel != null) {
            this.skillsLevel.clear(); // reset skills
        }
        if (this.skillExperience != null) {
            this.skillExperience.clear(); // reset skills
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

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public void setMaxEnergyTurn(int maxEnergyTurn) {
        this.maxEnergyTurn = maxEnergyTurn;
    }

    // the format to use this function user.addSkillExperience(Skill.FARMING);
    // use this function in farming fishing mining and foraging
    public void addSkillExperience(Skill skill) {
        int amount = skill.getXpPerAction();
        int currentLevel = skillsLevel.getOrDefault(skill, 0);
        int currentXP = skillExperience.getOrDefault(skill, 0);

        currentXP += amount;

        // update level
        while (currentLevel < 4 && currentXP >= 100 * currentLevel + 50) {
            currentXP -= 100 * currentLevel + 50;
            currentLevel++;
        }

        this.skillsLevel.put(skill, currentLevel);
        this.skillExperience.put(skill, currentXP);
    }

    public boolean isFainted() {
        return fainted;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public void setCurrentTool(ToolType currentTool) {
        this.currentTool = currentTool;
    }
}
