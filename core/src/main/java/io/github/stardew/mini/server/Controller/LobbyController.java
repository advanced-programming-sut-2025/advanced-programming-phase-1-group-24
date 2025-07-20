package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.LobbyInfo;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.server.Lobby;
import io.github.stardew.mini.server.LobbyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyController {
    public void createLobby(String name, String password, boolean isPrivate, boolean isInvisible, User user) {
        LobbyManager.getInstance().createLobby(name, password, isPrivate, isInvisible, user);
    }

    public Message<?> joinLobby(String id, String password, User user) {
        boolean canJoinLobby = LobbyManager.getInstance().joinLobby(id, password, user);
        Map<String, Object> body = new HashMap<>();
        if (canJoinLobby) {
            return new Message<>(200, "joined lobby", body, Message.MessageType.RESPONSE);
        }
        else
            return Message.FORBIDDEN.setMessage("You are not allowed to join this lobby");

    }
//    public Message<?> leaveLobby() {
//
//    }
    public Message<?> getAllLobbies() {
        return LobbyManager.getInstance().getAllLobbies();
    }

    public Message<?> leaveLobby(String id, User user) {
        for(Lobby lobby : LobbyManager.getInstance().getActiveLobbies().values()) {
            if (lobby.getId().equals(id)) {
                for (User users : lobby.getPlayers()) {
                    if(users.getUsername().equals(user.getUsername())) {
                        lobby.getPlayers().remove(user);
                        return new Message<>(200, "leaved lobby", null, Message.MessageType.RESPONSE);
                    }
                }
                return Message.NOT_FOUND.setMessage("player not found");
            }
        }
        return Message.NOT_FOUND.setMessage("lobby not found");
    }

    public Message<?> searchLobbyById(String id) {
        for(Lobby lobby : LobbyManager.getInstance().getActiveLobbies().values()) {
            System.out.println("lobby id: " + lobby.getId());
            System.out.println("String id :" + id);
            if(lobby.getId().equals(id)){
                Map<String, Object> body = new HashMap<>();
                LobbyInfo info = new LobbyInfo();
                info.setId(lobby.getId());
                info.setName(lobby.getName());
                info.setPlayerCount(lobby.getPlayers().size());
                info.setPrivate(lobby.isPrivate());
                info.setInvisible(lobby.isInvisible());
                body.put("lobby", info);
                return new Message<>(200, "found lobby", body, Message.MessageType.RESPONSE);
            }
        }
        return Message.NOT_FOUND.setMessage("No lobby with this ID");
    }
}
