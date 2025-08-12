package io.github.stardew.mini.common.Model;

import java.util.ArrayList;
import java.util.List;

public class LobbyInfo {
    private String id;
    private String name;
    private int playerCount;
    private boolean isPrivate;
    private boolean isInvisible;
    private List<String> players = new ArrayList<>();
    private String owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }
}
