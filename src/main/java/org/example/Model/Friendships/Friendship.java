package org.example.Model.Friendships;

import java.util.ArrayList;
import java.util.Objects;

public class Friendship {
    private final String player1;
    private final String player2;
    private int level;
    private int xp;
    private ArrayList<Gift> gifts = new ArrayList<>();
    private  ArrayList<Message> talkHistory = new ArrayList<>();

    public Friendship(String player1, String player2) {
        // Always store in lexicographical order to avoid duplicates
        if (player1.compareTo(player2) < 0) {
            this.player1 = player1;
            this.player2 = player2;
        } else {
            this.player1 = player2;
            this.player2 = player1;
        }
        this.level = 0;
        this.xp = 0;
        this.gifts = new ArrayList<>();
        this.talkHistory = new ArrayList<>();
    }

    public void addXp(int amount) {
        this.xp += amount;
        checkLevelUp();
    }

    public void subtractXp(int amount) {
        this.xp -= amount;
        if (xp < 0 && level > 0) {
            level--;
            xp = levelXpThreshold(level) - 10;
        } else if (xp < 0) {
            xp = 0;
        }
    }

    private void checkLevelUp() {
        int threshold = levelXpThreshold(level);
        while (xp >= threshold && level < 4) {
            xp -= threshold;
            level++;
            threshold = levelXpThreshold(level);
        }
    }

    private int levelXpThreshold(int level) {
        return 100 * (1 + level);
    }

    // Getters
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void addToGifts(Gift gift) {
        gifts.add(gift);
    }

    public ArrayList<Message> getTalkHistory() {
        return talkHistory;
    }

    public void setLevel(int level) {
        this.xp = 0;
        this.level = level;
    }

    // Optional: equals and hashCode so friendships can be used in Sets/Maps
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return player1.equals(that.player1) && player2.equals(that.player2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1, player2);
    }
}
