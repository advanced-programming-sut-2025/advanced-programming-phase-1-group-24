package io.github.stardew.mini.client;

import com.badlogic.gdx.Gdx;
import io.github.stardew.mini.Model.Friendships.Friendship;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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
//                if ("time-update".equalsIgnoreCase(message.getType())) {
//                    Map<String, Object> data = (Map<String, Object>) message.getBody();
//                    int hour = ((Double) data.get("hour")).intValue();
//                    int day = ((Double) data.get("day")).intValue();
//                    String dayOfWeekString = (String) data.get("dayOfWeek");
//                    String seasonString = (String) data.get("season");
//                    DayOfWeek dayOfWeek = DayOfWeek.fromString(dayOfWeekString);
//                    Season season = Season.fromString(seasonString);
//
//                    // Optional: store or update this data somewhere globally
//                    MainApp.getInstance().getCurrentGame().getTimeAndDate().updateTime(hour, day, dayOfWeek, season);
//
//                    // Notify your UI or game loop (if any)
//                    System.out.printf("[CLIENT] Time updated: %02d:00, Day %d (%s), Season: %s%n",
//                        hour, day, dayOfWeek, seasonString);
//                }
//            }
//
    ////        } else {
    ////            // Handle unsolicited messages if any (e.g., broadcasts)
    ////        }
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
                if ("time-update".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    int hour = ((Double) data.get("hour")).intValue();
                    int day = ((Double) data.get("day")).intValue();
                    String dayOfWeekString = (String) data.get("dayOfWeek");
                    String seasonString = (String) data.get("season");
                    DayOfWeek dayOfWeek = DayOfWeek.fromString(dayOfWeekString);
                    Season season = Season.fromString(seasonString);

                    // Optional: store or update this data somewhere globally
                    MainApp.getInstance().getCurrentGame().getTimeAndDate().updateTime(hour, day, dayOfWeek, season);

                    // Notify your UI or game loop (if any)
                    System.out.printf("[CLIENT] Time updated: %02d:00, Day %d (%s), Season: %s%n",
                        hour, day, dayOfWeek, seasonString);
                }
                if ("endOfDay".equalsIgnoreCase(message.getType())) {
                    Object bodyRaw = message.getBody();

                    if (bodyRaw instanceof Map<?, ?> bodyMap) {
                        Object gameJsonObj = bodyMap.get("game");

                        if (gameJsonObj instanceof  String json) {
                            System.out.println("////////////////////////////////////////////////////");
                            try {
                                Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                MainApp.getInstance().setCurrentGame(game);
                                game.reloadExtraData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.err.println("Response body is not a map");
                    }
                    System.out.printf("[CLIENT] Game updated handle end of day");
                }

                if ("start-game".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();

                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof  String json) {
                                    try {
                                        Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                        MainApp.getInstance().setCurrentGame(game);
                                        game.reloadExtraData();
                                       // System.out.println("Farms: " + MainApp.getInstance().getCurrentGame().getMap().getFarms().size());
                                        System.out.println("Game successfully deserialized");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("failed to deserialize game after map selection");
                                    }
                                }
                            } else {
                                System.err.println("Response body is not a map");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse game in start-map-selection");
                        }
                        MainApp.getInstance().setCurrentMenu(Menu.GameMenu); // Now the menu can read the game safely
                    });
                }
                if ("start-map-selection".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            System.out.println("addddddddddd//////////////////////////////////////////");
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof  String json) {
                                    try {
                                        Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                        MainApp.getInstance().setCurrentGame(game);
                                        game.reloadExtraData();
                                        // System.out.println("Farms: " + MainApp.getInstance().getCurrentGame().getMap().getFarms().size());
                                        System.out.println("Game successfully deserialized");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("failed to deserialize game after map selection");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse game in start-map-selection");
                        }
                        MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu); // Now the menu can read the game safely
                    });
                }
                if ("chat-message".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> chatData = (Map<String, Object>) message.getBody();
                    String sender = (String) chatData.get("sender");
                    String messageContent = (String) chatData.get("messageContent");
                    String chatType = (String) chatData.get("chatType");
                    String recipient = (String) chatData.get("recipient");

                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getChatDialogInstance() != null) {
                            if (Message.CHAT_PUBLIC.equals(chatType)) {
                                MainApp.getInstance().getChatDialogInstance().addPublicMessage(sender, messageContent);
                            } else if (Message.CHAT_PRIVATE.equals(chatType)) {
                                String currentUsername = MainApp.getInstance().getLoggedInUser().getUsername();
                                String chatPartner = sender.equals(currentUsername) ? recipient : sender;
                                MainApp.getInstance().getChatDialogInstance().addPrivateMessage(sender, chatPartner, messageContent);
                            }
                        } else {
                        }
                    });
                    return;
                }
                if (Message.POP_UP_NOTIFICATION.equalsIgnoreCase(message.getType())) {
                    Map<String, Object> notificationData = (Map<String, Object>) message.getBody();
                    String title = (String) notificationData.get("title");
                    String notificationBody = (String) notificationData.get("body");


                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showErrorDialog(MainApp.getInstance().getCurrentGameView().getStage(), notificationBody);
                        } else {
                            System.err.println("[DEBUG-NWCLIENT] GameView is null! Cannot show pop-up notification.");
                        }
                    });
                    return;
                }
                if (Message.REACTION_BROADCAST.equalsIgnoreCase(message.getType())) {
                    Map<String, Object> reactionData = (Map<String, Object>) message.getBody();
                    String sender = (String) reactionData.get("senderUsername");
                    String content = (String) reactionData.get("reactionContent");
                    boolean isImage = (Boolean) reactionData.get("isImage");

                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showReactionForPlayer(sender, content, isImage);
                        }
                    });
                    return;
                }
                if ("force_terminate_vote_started".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    String initiator = (String) data.get("initiator");
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showForceTerminationVoteDialog(initiator);
                        }
                    });
                } else if ("vote_cancelled".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().cancelTermination(message.getMessage());
                        }
                    });
                } else if ("game_terminated".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().handleGameTermination(message.getMessage());
                        }
                    });
                } else if ("vote_out_started".equalsIgnoreCase(message.getType())) {
                Map<String, Object> data = (Map<String, Object>) message.getBody();
                String initiator = (String) data.get("initiator");
                String target = (String) data.get("target");
                Gdx.app.postRunnable(() -> {
                    if (MainApp.getInstance().getCurrentGameView() != null) {
                        MainApp.getInstance().getCurrentGameView().showVoteOutConfirmationDialog(initiator, target);
                    }
                });
            } else if ("vote_out_result".equalsIgnoreCase(message.getType())) {
                Map<String, Object> data = (Map<String, Object>) message.getBody();
                String target = (String) data.get("target");
                String outcome = (String) data.get("outcome");
                String resultMessage = "The vote to eliminate " + target + " " + (outcome.equals("eliminated") ? "passed." : "failed.");

                // If the vote passed, remove the player from the local game state
                if ("eliminated".equals(outcome)) {
                    Game currentGame = MainApp.getInstance().getCurrentGame();
                    if (currentGame != null) {
                        currentGame.getPlayers().removeIf(p -> p.getUsername().equals(target));
                    }
                }

                Gdx.app.postRunnable(() -> {
                    if (MainApp.getInstance().getCurrentGameView() != null) {
                        MainApp.getInstance().getCurrentGameView().handlePlayerEliminated(target, resultMessage);
                    }
                });
            } else if ("you_were_eliminated".equalsIgnoreCase(message.getType())) {
                Gdx.app.postRunnable(() -> {
                    if (MainApp.getInstance().getCurrentGameView() != null) {
                        MainApp.getInstance().getCurrentGameView().handleYouAreEliminated(message.getMessage());
                    }
                });
            }  else if (Message.PLAYER_INTERACTION_BROADCAST.equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().handlePlayerInteraction(data);
                        }
                    });
                } else if (Message.MARRIAGE_PROPOSAL.equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    String proposer = (String) data.get("proposer");
                    Gdx.app.postRunnable(() -> {
                        if (MainApp.getInstance().getCurrentGameView() != null) {
                            MainApp.getInstance().getCurrentGameView().showMarriageProposalDialog(proposer);
                        }
                    });
                } else if (Message.MARRIAGE_RESPONSE_UPDATE.equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        Game game = MainApp.getInstance().getCurrentGame();
                        if (game == null) return;

                        Map<String, Object> data = (Map<String, Object>) message.getBody();
                        boolean accepted = (Boolean) data.get("accepted");
                        String proposerName = (String) data.get("proposer");
                        String responderName = (String) data.get("responder");

                        User proposer = game.getPlayerByUsername(proposerName);
                        User responder = game.getPlayerByUsername(responderName);
                        if (proposer == null || responder == null) return;

                        Friendship friendship = game.getFriendship(proposerName, responderName);
                        if (friendship == null) return;

                        int newLevel = ((Number) data.get("newFriendshipLevel")).intValue();
                        friendship.setLevel(newLevel);

                        if (accepted) {
                            int newMoney = ((Number) data.get("newMoney")).intValue();
                            proposer.setPartner(responder);
                            responder.setPartner(proposer);
                            proposer.setMoney(newMoney);
                            responder.setMoney(newMoney);

                            Item ring = proposer.getBackpack().grabItemAndReturn("Wedding Ring", 1);
                            if (ring != null) {
                                responder.getBackpack().addItem(ring, 1);
                                proposer.getBackpack().removeItem(ring.getName(),1);
                            }

                        } else {
                            int newEnergy = ((Number) data.get("proposerNewEnergy")).intValue();
                            proposer.setEnergy(newEnergy);
                            proposer.setDaysSinceRejection(7);
                        }
                    });
                } else if (Message.FRIENDSHIP_UPDATED.equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    Gdx.app.postRunnable(() -> {
                        Game game = MainApp.getInstance().getCurrentGame();
                        if (game != null) {
                            String p1 = (String) data.get("player1");
                            String p2 = (String) data.get("player2");
                            int level = ((Number) data.get("level")).intValue();
                            int xp = ((Number) data.get("xp")).intValue();
                            Friendship f = game.getFriendship(p1, p2);
                            if (f != null) {
                                f.setLevel(level);
                                // You might need a setXp method in Friendship if you want to sync it perfectly
                            }
                        }
                    });
                }
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
        requestMessage.setToken(MainApp.getInstance().getJwtToken());
        // Optionally include gameId in the body or add a field if needed (depends on server design)
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
        Map<String, Object> params,
        String username
    ) {
        return sendRequest(gameId, controllerName, methodName, "GET", params, username);
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
    /**
     * Send an AuthController.login request over WS, returning the server’s Message<String>.
     */
    public CompletableFuture<Message<String>> login(String username, String password) {
        // Build the credentials payload
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        // controllerName = "AuthController", methodName = "login"
        // No gameId, no username header needed here
        @SuppressWarnings("unchecked")
        CompletableFuture<Message<String>> future =
            (CompletableFuture<Message<String>>)(CompletableFuture<?>)
                sendRequest(
                    null,
                    "AuthController",
                    "login",
                    "POST",
                    params,
                    null
                );
        return future;
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

    public CompletableFuture<Message<?>> sendChatMessage(
        String gameId,
        String senderUsername,
        String messageContent,
        String recipientUsername,
        String chatType
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageContent", messageContent);
        params.put("chatType", chatType);
        if (recipientUsername != null) {
            params.put("recipientUsername", recipientUsername);
        }

        return sendPost(
            gameId,
            "GameController",
            "handleChatMessage",
            params,
            senderUsername
        );
    }

    public CompletableFuture<Message<?>> sendReaction(String gameId, String senderUsername, String reactionContent, boolean isImage) {
        Map<String, Object> params = new HashMap<>();
        params.put("reactionContent", reactionContent);
        params.put("isImage", isImage);

        return sendPost(
            gameId,
            "GameController",
            "handleReaction",
            params,
            senderUsername
        );
    }
}
