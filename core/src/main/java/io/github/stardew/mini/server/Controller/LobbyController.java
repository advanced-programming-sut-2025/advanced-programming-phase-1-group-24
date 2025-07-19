package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.server.Lobby;
import io.github.stardew.mini.server.LobbyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyController {
    public void createLobby(String name, String password, boolean isPrivate, User user) {
        LobbyManager.getInstance().createLobby(name, password, isPrivate, user);
    }

    //    public Message<?> joinLobby() {
//
//    }
//    public Message<?> leaveLobby() {
//    }
    public Message<?> getAllLobbies() {
        return LobbyManager.getInstance().getAllLobbies();
    }
    public Message<?> joinLobby(String id, String password, User user) {
        boolean canJoinLobby = LobbyManager.getInstance().joinLobby(id, password, user);
        if (canJoinLobby) {
            return new Message<>(200, "joined lobby", null, Message.MessageType.RESPONSE);
        }
        else
            return Message.FORBIDDEN.setMessage("You are not allowed to join this lobby");

    }
}
