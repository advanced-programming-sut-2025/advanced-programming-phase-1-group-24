package io.github.stardew.mini.server;

import io.github.stardew.mini.common.Model.User;
import io.javalin.websocket.WsContext;

public class PlayerConnection {
    private final String username;
    private final WsContext wsContext;
    private  User user;
    private long disconnectTime = -1;
    private boolean awaitingReconnect = false;

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

    public void setUser(User user) {
        this.user = user;
    }

    public void markDisconnected() {
        this.disconnectTime = System.currentTimeMillis();
        this.awaitingReconnect = true;
    }

    public boolean isAwaitingReconnect() {
        return awaitingReconnect;
    }

    public long getDisconnectTime() {
        return disconnectTime;
    }

    public void markReconnected() {
        this.awaitingReconnect = false;
        this.disconnectTime = -1;
    }
}

