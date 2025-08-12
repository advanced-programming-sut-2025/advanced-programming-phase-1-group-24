package io.github.stardew.mini.server;

import io.github.stardew.mini.common.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    private String id;
    private String name;
    private String password;
    private List<User> players;
    private User creator;
    private boolean started;
    private Map<String, Boolean> mapSelectionState = new HashMap<>();
    private boolean isPrivate;
    private boolean isInvisible;
    private long createdAt;

    public static final int MAX_PLAYERS = 4;

    public Lobby(String id, String name, String password, User creator, boolean started, boolean isPrivate, boolean isInvisible) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.creator = creator;
        this.players = new ArrayList<>();
        this.started = started;
        this.isPrivate = isPrivate;
        this.isInvisible = isInvisible;
        for(User user : getPlayers()) {
            mapSelectionState.put(user.getUsername(), false);
        }
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis();
        return (players.isEmpty() || (now - createdAt) > (1 * 60 * 1000));
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setMapSelectionState(Map<String, Boolean> mapSelectionState) {
        this.mapSelectionState = mapSelectionState;
    }

    public User getCreator() {
        return creator;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<User> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Map<String, Boolean> getMapSelectionState() {
        return mapSelectionState;
    }
    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }
}
