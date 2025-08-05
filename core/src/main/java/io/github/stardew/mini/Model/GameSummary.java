package io.github.stardew.mini.Model;

import java.util.List;
import java.util.stream.Collectors;

public class GameSummary {
    private String gameId;
    private String ownerUsername;
    private List<String> playerUsernames;
    private int playerCount;
    private String lastSavedDate;

    public GameSummary(String gameId, String ownerUsername, List<String> playerUsernames, String lastSavedDate) {
        this.gameId = gameId;
        this.ownerUsername = ownerUsername;
        this.playerUsernames = playerUsernames;
        this.playerCount = playerUsernames.size();
        this.lastSavedDate = lastSavedDate;
    }
    public static GameSummary fromGame(Game game) {
        List<String> usernames = game.getPlayers().stream()
            .map(User::getUsername)
            .collect(Collectors.toList());
        return new GameSummary(
            game.getNetworkId(),
            game.getMainPlayer().getUsername(),
            usernames,
            game.getTimeAndDate().formattedTime() // Or a formatted version
        );
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<String> getPlayerUsernames() {
        return playerUsernames;
    }

    public void setPlayerUsernames(List<String> playerUsernames) {
        this.playerUsernames = playerUsernames;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public String getLastSavedDate() {
        return lastSavedDate;
    }

    public void setLastSavedDate(String lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }
    // Getters and setters
}

