package io.github.stardew.mini.Model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Friendships.FriendshipMessage;
import io.github.stardew.mini.Model.Friendships.Gift;
import io.github.stardew.mini.Model.Friendships.Trade;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Reccepies.FoodRecipe;
import io.github.stardew.mini.Model.Reccepies.MachineType;
import io.github.stardew.mini.Model.Things.Food;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.Tools.Tool;
import io.github.stardew.mini.Model.Things.*;
import io.github.stardew.mini.Model.Reccepies.*;
import io.github.stardew.mini.Model.Growables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class User {
    //user
    private String username;
    private String password;
    private String nickname;
    private String email;
    private boolean gender;
    private int playedGames;
    private int maxMoneyInGames;
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
    private Map<Skill, Integer> skillsLevel;
    private Map<Skill, Integer> skillExperience;
    private Tile currentTile;
    private Tool equippedTool;
    private ArrayList<MachineType> machineRecepies;
    private ArrayList<FoodRecipe> cookingRecepies;
    private Map<User, FriendshipLevels> friends;
    private Backpack backpack;
    private ArrayList<Animal> ownedAnimals=new ArrayList<>();
    private Tile homeTile;
    private ArrayList<FriendshipMessage> notifications = new ArrayList<>();
    private ArrayList<Gift> recievedGift = new ArrayList<>();
    private User partner;
    private int daysSinceRejection;
    private ArrayList<Trade> tradingHistory = new ArrayList<>();
    private ArrayList<FriendshipMessage> tradeNotifications = new ArrayList<>();

    private boolean buffMaxEnergy;
    private boolean buffForagingSkill;
    private boolean buffFarmingSkill;
    private boolean buffFishingSkill;
    private boolean buffMiningSkill;
    private int hoursLeftForBuff;
    private int movingDirection = 0;

    private Avatar avatar;
    private boolean isProposing;
    private boolean isAccepting;
    private boolean isRejecting;
    private float proposingTimer = 0f;
    private float acceptingTimer = 0f;
    private float rejectingTimer = 0f;

    public float getProposingTimer() {
        return proposingTimer;
    }

    public float getAcceptingTimer() {
        return acceptingTimer;
    }

    public float getRejectingTimer() {
        return rejectingTimer;
    }

    public void setProposingTimer(float proposingTimer) {
        this.proposingTimer = proposingTimer;
    }

    public void setAcceptingTimer(float acceptingTimer) {
        this.acceptingTimer = acceptingTimer;
    }

    public void setRejectingTimer(float rejectingTimer) {
        this.rejectingTimer = rejectingTimer;
    }

    public boolean isRejecting() {
        return isRejecting;
    }

    public void setRejecting(boolean rejecting) {
        isRejecting = rejecting;
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    public void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    public boolean isProposing() {
        return isProposing;
    }

    public void setProposing(boolean proposing) {
        isProposing = proposing;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public int getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(int movingDirection) {
        this.movingDirection = movingDirection;
    }

    /// /////////////?????????????/
    public User(){}
    public User(String username, String password, String nickname, String email, boolean gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;

        if(gender){
            this.avatar = Avatar.Abigail;
        }
        else{
            this.avatar = Avatar.Alex;
        }

        this.skillsLevel = new HashMap<>();
        for (Skill skill : Skill.values()) {
            skillsLevel.put(skill, 0);
        }
        this.skillExperience = new HashMap<>();
        for (Skill skill : Skill.values()) {
            skillExperience.put(skill, 0);
        }

        this.ownedAnimals = new ArrayList<>();
        this.machineRecepies = new ArrayList<>();
        this.cookingRecepies = new ArrayList<>();
        this.friends = new HashMap<>();
        this.backpack = new Backpack();
        this.notifications = new ArrayList<>();
        this.recievedGift = new ArrayList<>();
        this.cookingRecepies.add(FoodRecipe.Salad);
        this.cookingRecepies.add(FoodRecipe.BakedFish);
        this.cookingRecepies.add(FoodRecipe.FriedEgg);
        this.partner = null;
        this.daysSinceRejection = 0;
        this.tradingHistory = new ArrayList<>();
        this.tradeNotifications = new ArrayList<>();
        this.buffFarmingSkill = false;
        this.buffForagingSkill = false;
        this.buffFishingSkill = false;
        this.buffMiningSkill = false;
        this.buffMaxEnergy = false;
        this.hoursLeftForBuff = 0;
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



    public ArrayList<FoodRecipe> getCookingRecepies() {
        if (cookingRecepies == null)
            cookingRecepies = new ArrayList<>();
        return cookingRecepies;
    }

    public Map<User, FriendshipLevels> getFriends() {
        if (friends == null)
            friends = new HashMap<>();
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


    public void updateGameFields() {
        this.maxEnergy = 200;
        this.maxEnergyTurn = 50;
        //this.playedGames += 1;
        this.energy = maxEnergy;
        this.money = 0;
        this.currentTurnEnergy = maxEnergyTurn;

        this.currentTile = null;
        this.equippedTool = null;
        this.backpack = new Backpack(); // reset

        this.ownedAnimals = new ArrayList<>();
        this.skillExperience = new HashMap<>();
        this.machineRecepies = new ArrayList<>();
        this.cookingRecepies = new ArrayList<>();
        this.friends = new HashMap<>();
        this.skillsLevel = new HashMap<>();
        this.notifications = new ArrayList<>();
        for (Skill skill : Skill.values()) {
            skillsLevel.put(skill, 0);
        }

        this.skillExperience = new HashMap<>();
        for (Skill skill : Skill.values()) {
            skillExperience.put(skill, 0);
        }
        this.tradingHistory = new ArrayList<>();
        this.tradeNotifications = new ArrayList<>();
        this.cookingRecepies.add(FoodRecipe.Salad);
        this.cookingRecepies.add(FoodRecipe.BakedFish);
        this.cookingRecepies.add(FoodRecipe.FriedEgg);
        this.recievedGift = new ArrayList<>();
        this.partner = null;
        this.fainted = false;
        this.buffFarmingSkill = false;
        this.buffForagingSkill = false;
        this.buffFishingSkill = false;
        this.buffMiningSkill = false;
        this.buffMaxEnergy = false;
        this.hoursLeftForBuff = 0;
        this.isProposing = false;
        this.isAccepting = false;
        this.isRejecting = false;
        this.proposingTimer = 0f;
        this.acceptingTimer = 0f;
        this.rejectingTimer = 0f;
    }



    public void updateMaxMoney() {
        if (money > maxMoneyInGames) {
            maxMoneyInGames = money;
        }
    }

    public void resetEnergyForNewDay() {
        this.currentTurnEnergy = maxEnergyTurn;
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

    public void perfectFishingSkillUpgrade() {
        int currentXP = skillExperience.getOrDefault(Skill.FISHING, 0);
        int currentLevel = skillsLevel.getOrDefault(Skill.FISHING, 0);

        currentXP *= 2.4;

        while (currentLevel < 4 && currentXP >= 100 * currentLevel + 50) {
            currentXP -= 100 * currentLevel + 50;
            currentLevel++;
        }

        this.skillsLevel.put(Skill.FISHING, currentLevel);
        this.skillExperience.put(Skill.FISHING, currentXP);
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getMaxEnergyTurn() {
        return maxEnergyTurn;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public void setFriends(Map<User, FriendshipLevels> friends) {
        this.friends = friends;
    }

    public void setCookingRecepies(ArrayList<FoodRecipe> cookingRecepies) {
        this.cookingRecepies = cookingRecepies;
    }

    public ArrayList<MachineType> getMachineRecepies() {
        return machineRecepies;
    }

    public void setMachineRecepies(ArrayList<MachineType> machineRecepies) {
        this.machineRecepies = machineRecepies;
    }

    public Map<Skill, Integer> getSkillsLevel() {
        return skillsLevel;
    }

    public int giveSkillLevel(Skill skill) {
        return skillsLevel.getOrDefault(skill, 0);
    }

    public void setSkillsLevel(Map<Skill, Integer> skillsLevel) {
        this.skillsLevel = skillsLevel;
    }

    public Map<Skill, Integer> getSkillExperience() {
        return skillExperience;
    }

    public void setSkillExperience(Map<Skill, Integer> skillExperience) {
        this.skillExperience = skillExperience;
    }

    public Animal getAnimalByName(String name) {
        for (Animal animal : ownedAnimals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null;
    }

    public ArrayList<Animal> getOwnedAnimals() {
        if (ownedAnimals == null) {
            ownedAnimals = new ArrayList<>();
        }
        return ownedAnimals;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username); // or a unique ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(username); // match the field used in equals
    }

    public Tool getEquippedTool() {
        return equippedTool;
    }

    public void setEquippedTool(Tool equippedTool) {
        this.equippedTool = equippedTool;
    }

    //always call this function before any task that consumes energy if it returns false cant do the task
    public boolean tryConsumeEnergy(int energyRequired) {
//        if (currentTurnEnergy < energyRequired || energy < energyRequired) {
//            //System.out.println("not enough energy!");
//            return false;
//        }
        if(energy < energyRequired){
            return false;
        }
        //currentTurnEnergy -= energyRequired;
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
        if (this.energy <= 0) { //deleted turn enrgy
            this.energy = 0;
            this.currentTurnEnergy = 0;
            System.out.println("not enough energy! You faiented!");
            this.fainted = true;
        }
    }

    public void addMoney(int sellingPrice) {
        if(this.partner!= null){
            this.partner.setMoney(this.partner.getMoney() + sellingPrice);
        }
        money += sellingPrice;
    }

    public void decreaseMoney(int sellingPrice) {
        if(this.partner!= null){
            this.partner.setMoney(this.partner.getMoney() - sellingPrice);
        }
        money -= sellingPrice;
    }

    public Tile getHomeTile() {
        return homeTile;
    }

    public void setHomeTile(Tile homeTile) {
        this.homeTile = homeTile;
    }

    public void addEnergy(int amount) {
        if (amount < 0) return;
        this.energy = Math.min(this.energy + amount, maxEnergy);
        this.currentTurnEnergy = Math.min(this.currentTurnEnergy + amount, maxEnergyTurn);
    }

    public ArrayList<FriendshipMessage> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<FriendshipMessage> notifications) {
        this.notifications = notifications;
    }

    public void addToNotifications(FriendshipMessage message) {
        this.notifications.add(message);
    }

    public void addRecievedGift(Gift gift) {
        this.recievedGift.add(gift);
    }

    public ArrayList<Gift> getRecievedGift() {
        return recievedGift;
    }

    public void setRecievedGift(ArrayList<Gift> recievedGift) {
        this.recievedGift = recievedGift;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }
    public int getDaysSinceRejection() {
        return daysSinceRejection;
    }
    public void setDaysSinceRejection(int daysSinceRejection) {
        this.daysSinceRejection = daysSinceRejection;
    }

    public ArrayList<Trade> getTradingHistory() {
        return tradingHistory;
    }

    public ArrayList<FriendshipMessage> getTradeNotifications() {
        return tradeNotifications;
    }

    public void setTradingHistory(ArrayList<Trade> tradingHistory) {
        this.tradingHistory = tradingHistory;
    }

    public void setTradeNotifications(ArrayList<FriendshipMessage> tradeNotifications) {
        this.tradeNotifications = tradeNotifications;
    }

    public User(String username, String password) {
        this(username, password, "defaultNick", "default@email.com", true);
    }

    public void handleSpecialFoodsEffects() {  //add this to the method that handles each hour
        if (this.hoursLeftForBuff > 0) hoursLeftForBuff--;
        if (hoursLeftForBuff == 0) {
            if (buffMaxEnergy) {
                maxEnergy = 200;
                if (energy > 200) energy = 200;
                buffMaxEnergy = false;
            }

            if (buffForagingSkill) {
                buffForagingSkill = false;
            }

            if (buffFarmingSkill){
                buffFarmingSkill = false;
            }

            if (buffMiningSkill) {
                buffMiningSkill = false;
            }

            if (buffFishingSkill) {
                buffFishingSkill = false;
            }
        }
    }

    public void cancelAllBuffs() {
        if (buffMaxEnergy) {
            buffMaxEnergy = false;
            hoursLeftForBuff = 0;
            this.maxEnergy = 200;
            if (this.energy > 200) energy = 200;
        }
        else if (buffForagingSkill) {
            buffForagingSkill = false;
            hoursLeftForBuff = 0;
        }
        else if (buffFarmingSkill) {
            buffFarmingSkill = false;
            hoursLeftForBuff = 0;
        }
        else if (buffMiningSkill) {
            buffMiningSkill = false;
            hoursLeftForBuff = 0;
        }
        else if (buffFishingSkill) {
            buffFishingSkill = false;
            hoursLeftForBuff = 0;
        }
    }

    public Result eat(Item item) {
        if (!item.isEatable())
            return new Result(false, "You cant eat this item!");
        else if (item instanceof Food) {
            addEnergy(((Food) item).getEnergy());
            if (item.getName().equals(FoodType.Triple_Shot_Espresso.getName())) {
                cancelAllBuffs();
                this.maxEnergy = 300;
                this.energy += 100;
                this.hoursLeftForBuff = 5;
                this.buffMaxEnergy = true;
            }
            else if (item.getName().equals(FoodType.Red_Plate.getName())) {
                cancelAllBuffs();
                this.maxEnergy = 250;
                this.energy += 50;
                this.hoursLeftForBuff = 3;
                this.buffMaxEnergy = true;
            }
            else if (item.getName().equals(FoodType.Hashbrowns.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 5;
                this.buffFarmingSkill = true;
            }
            else if (item.getName().equals("Pancakes")) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 11;
                this.buffForagingSkill = true;
            }
            else if (item.getName().equals(FoodType.Survival_Burger.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 5;
                this.buffForagingSkill = true;
            }
            else if (item.getName().equals(FoodType.Farmers_Lunch.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 5;
                this.buffFarmingSkill = true;
            }
            else if (item.getName().equals(FoodType.Dish_O_The_Sea.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 5;
                this.buffFishingSkill = true;

            }
            else if (item.getName().equals(FoodType.Seafoam_Pudding.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 10;
                this.buffFishingSkill = true;
            }
            else if (item.getName().equals(FoodType.Miners_Treat.getName())) {
                cancelAllBuffs();
                this.hoursLeftForBuff = 5;
                this.buffMiningSkill = true;
            }
        }
        else if (item instanceof randomStuff) {
            if (((randomStuff) item).getType().getEatable()) {
                addEnergy(((randomStuff) item).getType().getEnergy());
            }
            else return new Result(false, "You cant eat this item!");
        }
        else if (item instanceof Growable) {
            if (((Growable) item).getGrowableType().equals(GrowableType.Fruit)) {
                if (((Growable) item).getTreeType().getFruitType().getIsFruitEdible()) {
                    addEnergy(((Growable) item).getTreeType().getFruitType().getFruitEnergy());
                }
                else return new Result(false, "You cant eat this item!");
            }
            else if (((Growable) item).getGrowableType().equals(GrowableType.CropProduct)){
                if (((Growable) item).getCropType().getIsEdible()) {
                    addEnergy(((Growable) item).getCropType().getEnergy());
                }
                else return new Result(false, "You cant eat this item!");
            }
            else if (((Growable) item).getGrowableType().equals(GrowableType.ForagingCrop)){
                addEnergy(((Growable) item).getForagingCropType().getEnergy());
            }
        }
        this.getBackpack().grabItem(item.getName(),1);
        return new Result(true, "Item eaten successfully!");
    }
    public boolean isBuffMaxEnergy() {
        return buffMaxEnergy;
    }

    public boolean isBuffForagingSkill() {
        return buffForagingSkill;
    }

    public boolean isBuffFarmingSkill() {
        return buffFarmingSkill;
    }

    public boolean isBuffFishingSkill() {
        return buffFishingSkill;
    }

    public boolean isBuffMiningSkill() {
        return buffMiningSkill;
    }

    public int getHoursLeftForBuff() {
        return hoursLeftForBuff;
    }
}

