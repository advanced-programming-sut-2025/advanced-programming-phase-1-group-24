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
    int energy = 200;
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

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }

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

    // public Shop getCurrentShop() {
    //     return currentShop;
    // }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public ToolType getCurrentTool() {
        return currentTool;
    }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setEmail(String email) { this.email = email; }

    public void faint(){};
    public void setEnergy(int energy) { this.energy = energy; }

    public void trade(){}

}
