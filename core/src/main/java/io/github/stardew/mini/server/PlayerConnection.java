package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.User;
import io.javalin.websocket.WsContext;

public class PlayerConnection {
    private final String username;
    private final WsContext wsContext;
    private final User user;

    public PlayerConnection(String username, WsContext wsContext) {
        this.username = username;
        this.wsContext = wsContext;
        this.user = ServerApp.getInstance().getUserByUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public User getUser() {
        return user;
    }

    public WsContext getWsContext() {
        return wsContext;
    }

    public void send(String json) {
        if (wsContext.session.isOpen()) {
            wsContext.send(json);
        }
    }
}
