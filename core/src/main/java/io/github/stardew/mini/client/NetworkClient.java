package io.github.stardew.mini.client;

import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class NetworkClient extends WebSocketClient {
//    public void onLoginSuccess(User loggedInUser) {
//        NetworkClient.connectWebSocket(loggedInUser.getUsername());
//        MainApp.getInstance().setLoggedInUser(loggedInUser);
//        MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
//    }


    private static final Gson gson = new Gson();

    // Map requestId -> CompletableFuture to complete on response
    private final Map<String, CompletableFuture<Message<?>>> pendingRequests = new ConcurrentHashMap<>();

    public NetworkClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
        // You can send a handshake message if needed
        // send("{\"type\": \"connect\", \"username\": \"zahraa\"}");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);

        // Parse the incoming JSON message
        JsonObject json = gson.fromJson(message, JsonObject.class);

        // Extract requestId from the response to match it to a request
        if (json.has("requestId")) {
            String requestId = json.get("requestId").getAsString();

            // Complete the waiting CompletableFuture with the response Message
            CompletableFuture<Message<?>> future = pendingRequests.remove(requestId);
            if (future != null) {
                Message<?> response = gson.fromJson(message, Message.class);
                future.complete(response);
            }
        } else {
            // Handle other incoming server messages (broadcasts, etc.)
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed: " + reason);
        // Optionally fail all pending requests
        pendingRequests.forEach((id, future) -> future.completeExceptionally(new RuntimeException("Connection closed")));
        pendingRequests.clear();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Send a request over WebSocket and return a CompletableFuture that completes when response arrives.
     */
    public CompletableFuture<Message<?>> sendRequest(String controllerName, String methodName, String type, Map<String, Object> params) {
        if (!isOpen()) {
            CompletableFuture<Message<?>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new IllegalStateException("WebSocket is not open"));
            return failedFuture;
        }

        String requestId = UUID.randomUUID().toString();

        // Build the request message object
        Message<Map<String, Object>> requestMessage = new Message<>(0, "Client Request", params, Message.MessageType.REQUEST);
        requestMessage.setType(type);
        requestMessage.setUsername(null); // or set if you have username
        // add your requestId for correlation
        JsonObject jsonObject = (JsonObject) gson.toJsonTree(requestMessage);
        jsonObject.addProperty("controllerName", controllerName);
        jsonObject.addProperty("methodName", methodName);
        jsonObject.addProperty("requestId", requestId);

        String json = gson.toJson(jsonObject);

        CompletableFuture<Message<?>> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        send(json);

        return future;
    }

    // Convenience methods to simulate GET and POST (by convention)
    public CompletableFuture<Message<?>> sendGet(String controllerName, String methodName, Map<String, Object> params) {
        return sendRequest(controllerName, methodName, "GET", params);
    }

    public CompletableFuture<Message<?>> sendPost(String controllerName, String methodName, Map<String, Object> params) {
        return sendRequest(controllerName, methodName, "POST", params);
    }
}
