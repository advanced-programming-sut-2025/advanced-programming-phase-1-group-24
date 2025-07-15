package io.github.stardew.mini.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class NetworkClient extends WebSocketClient {
//class usage example:
//public void onLoginSuccess(User loggedInUser) {
//    NetworkClient.connectWebSocket(loggedInUser.getUsername());
//    MainApp.getInstance().setLoggedInUser(loggedInUser);
//    MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
//}
    public NetworkClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
        send("{\"type\": \"connect\", \"username\": \"zahraa\"}");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}

