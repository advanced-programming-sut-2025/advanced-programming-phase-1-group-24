package io.github.stardew.mini.client;

import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class NetworkClient extends WebSocketClient {

    private static final Gson gson = new Gson();

    // Map to track requests waiting for a response by requestId
    private final Map<String, CompletableFuture<Message<?>>> pendingRequests = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public NetworkClient(URI serverUri) {
        super(serverUri);
    }

    //    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("WebSocket connected");
//    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connected");

        // Schedule ping every 10 seconds
        scheduler.scheduleAtFixedRate(() -> {
            if (this.isOpen()) {
                try {
                    this.send("ping"); // or a proper ping JSON if your server expects it
                } catch (Exception e) {
                    System.err.println("Failed to send ping: " + e.getMessage());
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }


//    @Override
//    public void onMessage(String messageJson) {
//        System.out.println("Received: " + messageJson);
//
//        // Deserialize incoming message to Message class
//        Message<?> message = gson.fromJson(messageJson, Message.class);
//
//        String requestId = message.getRequestId();
//        if (requestId != null) {
//            CompletableFuture<Message<?>> future = pendingRequests.remove(requestId);
//            if (future != null) {
//                future.complete(message);
//            }
//        } else {
//            // Handle unsolicited messages if any (e.g., broadcasts)
//        }
//    }

    @Override
    public void onMessage(String messageJson) {
        System.out.println("Received raw JSON: " + messageJson);

        try {
            Message<?> message = gson.fromJson(messageJson, Message.class);
            System.out.println("Parsed message object: " + message);
            System.out.println("RequestId: " + message.getRequestId());

            String requestId = message.getRequestId();
            if (requestId != null) {
                CompletableFuture<Message<?>> future = pendingRequests.remove(requestId);
                if (future != null) {
                    System.out.println("✅ Completing future for requestId: " + requestId);
                    future.complete(message);
                } else {
                    System.err.println("❌ No future found for requestId: " + requestId);
                }
            } else {
                System.err.println("❌ requestId was null");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to parse message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("WebSocket closed: " + reason);
//        // Fail all pending requests
//        pendingRequests.forEach((id, future) -> future.completeExceptionally(
//            new RuntimeException("Connection closed before response")));
//        pendingRequests.clear();
//    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + reason);
        scheduler.shutdownNow(); // stop pings
        pendingRequests.forEach((id, future) -> future.completeExceptionally(
            new RuntimeException("Connection closed before response")));
        pendingRequests.clear();
    }


    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Send a request using Message object and return CompletableFuture for the response.
     */
    public CompletableFuture<Message<?>> sendRequest(
        String gameId,
        String controllerName,
        String methodName,
        String httpMethod,
        Map<String, Object> params,
        String username // optionally pass username if you want
    ) {
        if (!isOpen()) {
            CompletableFuture<Message<?>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new IllegalStateException("WebSocket is not open"));
            return failedFuture;
        }

        String requestId = UUID.randomUUID().toString();

        // Create Message object with all relevant info
        Message<Map<String, Object>> requestMessage = new Message<>(0, "Client Request", params, Message.MessageType.REQUEST);
        requestMessage.setControllerName(controllerName);
        requestMessage.setMethodName(methodName);
        requestMessage.setRequestId(requestId);
        requestMessage.setType(httpMethod); // "GET" or "POST"
        requestMessage.setUsername(username);
        requestMessage.setMessageType(Message.MessageType.REQUEST);

//        // Optionally include gameId in the body or add a field if needed (depends on server design)
//        if (params != null && gameId != null) {
//            params.put("gameId", gameId);
//        }
        requestMessage.setGameID(gameId);

        // Serialize and send
        String json = gson.toJson(requestMessage);

        System.out.println("Sending JSON: " + json);

        CompletableFuture<Message<?>> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        send(json);

        return future;
    }

    // Convenience overloads without username parameter:

    public CompletableFuture<Message<?>> sendGet(
        String gameId,
        String controllerName,
        String methodName,
        Map<String, Object> params
    ) {
        return sendRequest(gameId, controllerName, methodName, "GET", params, null);
    }

    public CompletableFuture<Message<?>> sendPost(
        String gameId,
        String controllerName,
        String methodName,
        Map<String, Object> params,
        String username
    ) {
        return sendRequest(gameId, controllerName, methodName, "POST", params, username);
    }

    public void sendConnect(String username) {
        if (!isOpen()) {
            System.err.println("WebSocket is not open");
            return;
        }

        Message<String> connectMsg = new Message(0, "connect", null, Message.MessageType.REQUEST);
        connectMsg.setUsername(username);

        String json = gson.toJson(connectMsg);
        send(json);
        System.out.println("Sent connect for user: " + username);
    }

}
