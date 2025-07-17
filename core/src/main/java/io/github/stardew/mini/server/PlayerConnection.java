package io.github.stardew.mini.server;

import io.javalin.websocket.WsContext;

public class PlayerConnection {
    private final String username;
    private final WsContext wsContext;

    public PlayerConnection(String username, WsContext wsContext) {
        this.username = username;
        this.wsContext = wsContext;
    }

    public String getUsername() {
        return username;
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
