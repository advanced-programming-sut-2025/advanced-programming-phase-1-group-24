package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.LobbyInfo;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.User;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LobbyManager {
    private static LobbyManager instance = new LobbyManager();
    private Map<String, Lobby> activeLobbies = new ConcurrentHashMap<>();


    private LobbyManager() {}

    public static LobbyManager getInstance() {
        return instance;
    }

    public Lobby createLobby(String name, String password, boolean isPrivate, User creator) {
        String id = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(id, name, password, creator, false,isPrivate);
        activeLobbies.put(id, lobby);
        joinLobby(id, password, creator);
        return lobby;
    }

    public List<Lobby> listAvailableLobbies() {
        return activeLobbies.values().stream()
            .filter(lobby -> !lobby.isPrivate() && !lobby.isStarted())
            .collect(Collectors.toList());
    }

//    public Message<?> getAllLobbies() {
//        List<Lobby> lobbies = LobbyManager.getInstance().listAvailableLobbies();
//        List<LobbyInfo> LobbyInfoList = lobbies.stream().map(
//            Lobby -> {
//                LobbyInfo info = new LobbyInfo();
//                info.setId(Lobby.getId());
//                info.setName(Lobby.getName());
//                info.setPlayerCount(Lobby.getPlayers().size());
//                info.setPrivate(Lobby.isPrivate());
//                return info;
//            }).collect(Collectors.toList());
//        Map<String, Object> body = new HashMap<>();
//        body.put("lobbies", LobbyInfoList);
//        return new Message<>(200, "sent all lobies", body, Message.MessageType.RESPONSE);
//    }
public Message<?> getAllLobbies() {
    List<Lobby> lobbies = LobbyManager.getInstance().listAvailableLobbies();
    List<LobbyInfo> lobbyInfoList = lobbies.stream().map(lobby -> {
        LobbyInfo info = new LobbyInfo();
        info.setId(lobby.getId());
        info.setName(lobby.getName());
        info.setOwner(lobby.getCreator().getUsername());
        List<String> usernames = lobby.getPlayers().stream()
            .map(User::getUsername)
            .collect(Collectors.toList());

        info.setPlayerCount(usernames.size());
        info.setPlayers(usernames);
        info.setPrivate(lobby.isPrivate());
        return info;
    }).collect(Collectors.toList());

    Map<String, Object> body = new HashMap<>();
    body.put("lobbies", lobbyInfoList);
    return new Message<>(200, "sent all lobbies", body, Message.MessageType.RESPONSE);
}


    public boolean joinLobby(String id, String password, User user) {
        Lobby lobby = activeLobbies.get(id);
        if (lobby != null && lobby.getPlayers().size() < Lobby.MAX_PLAYERS) {
            if (lobby.isPrivate() && !lobby.getPassword().equals(password)) return false;
            lobby.getPlayers().add(user);
            return true;
        }
        return false;
    }

    public void removeLobbyIfEmpty(String id) {
        Lobby lobby = activeLobbies.get(id);
        if (lobby != null && lobby.getPlayers().isEmpty()) {
            activeLobbies.remove(id);
        }
    }

    public void startGame(String id) {
        Lobby lobby = activeLobbies.get(id);
        if (lobby != null && lobby.getPlayers().size() >= 2 ) {
            lobby.setStarted(true);
            // transition to game session
        }
    }
}
