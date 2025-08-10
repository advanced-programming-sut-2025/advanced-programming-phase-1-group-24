
package io.github.stardew.mini.client;

import com.badlogic.gdx.Gdx;
import io.github.stardew.mini.Model.Friendships.Friendship;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.GameAudioManager;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.View.GameView;
import io.github.stardew.mini.client.View.MainMenuView;
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.PlayerConnection;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.View.MainMenuView;
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.PlayerConnection;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import java.net.URI;
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

                if ("online-players".equalsIgnoreCase(message.getType())) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> body = (Map<String,Object>) message.getBody();
                    @SuppressWarnings("unchecked")
                    List<Map<String,String>> list = (List<Map<String,String>>) body.get("players");
                    MainApp.getInstance().updateOnlinePlayers(list);
                    return;
                }

                if ("player-disconnected".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    String username = (String) data.get("username");

                    Gdx.app.postRunnable(() -> {
                        // نمایش دیالوگ یا پیام برای DC
                        MainApp.getInstance().showPlayerDisconnectedMessage(username);
                    });
                    return;
                }


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
                                game.reloadExtraData();
                                MainApp.getInstance().setCurrentGame(game);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.err.println("Response body is not a map");
                    }
                    System.out.printf("[CLIENT] Game updated handle end of day");
                }
                if ("shop-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        Object bodyRaw = message.getBody();
                        if (bodyRaw instanceof Map<?, ?> bodyMap) {
                            Object gameJson = bodyMap.get("game");
                            if (gameJson instanceof String json) {
                                try {
                                    Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                    MainApp.getInstance().setCurrentGame(game);
                                    game.reloadExtraData();
                                    System.out.println("[CLIENT] Shop state updated.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
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
                                        game.reloadExtraData();
                                        MainApp.getInstance().setCurrentGame(game);
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
                                        game.reloadExtraData();
                                        MainApp.getInstance().setCurrentGame(game);
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
                if ("game-ended".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        MainApp.getInstance().setCurrentGame(null);
                        MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
//                        MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
                    });
                }
                if ("faint-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object faintedUserObj = bodyMap.get("fainted");
                                if (faintedUserObj instanceof String faintedUsername) {
                                    User user = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
                                    if (user != null && user.getUsername().equals(faintedUsername)) {
                                        user.setFainted(true);
                                    } else {
                                        System.err.println("User not found: " + faintedUsername);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse fainted User");
                        }
                    });
                }
//                if ("leaderboard-update".equalsIgnoreCase(message.getType())) {
//                    @SuppressWarnings("unchecked")
//                    List<Map<String,Object>> lb =
//                        (List<Map<String,Object>>)((Map<?,?>)message.getBody()).get("leaderboard");
//
//                    Gdx.app.postRunnable(() ->
//                        MainApp.getInstance().getCurrentGameView().updateLeaderboard(lb)
//                    );
//                    return;
//                }
                if ("leaderboard-update".equalsIgnoreCase(message.getType())) {
                    @SuppressWarnings("unchecked")
                    List<Map<String,Object>> lb =
                        (List<Map<String,Object>>)((Map<?,?>)message.getBody()).get("leaderboard");

                    GameView view = MainApp.getInstance().getCurrentGameView();
                    if (view != null) {
                        Gdx.app.postRunnable(() -> view.updateLeaderboard(lb));
                    } else {
                        System.out.println("⚠️ Warning: GameView is null. Dropping leaderboard update.");
                    }
                    return;
                }

//                if ("radio-update".equalsIgnoreCase(message.getType())) {
//                    @SuppressWarnings("unchecked")
//                    Map<String,Object> b = (Map<String,Object>) message.getBody();
//                    String trackId = (String) b.get("trackId");
//                    String base64  = (String) b.get("data");
//
//                    if (base64 == null || base64.isEmpty()) {
//                        System.err.println("Empty base64 data for track: " + trackId);
//                        return;
//                    }
//
//                    // پاک کردن فایل‌های قدیمی tmp
//                    for (FileHandle old : Gdx.files.local(".").list()) {
//                        if (old.name().startsWith("radio_tmp_")) {
//                            old.delete();
//                        }
//                    }
//
//                    // decode with java.util.Base64 (safer compatibility with server)
//                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64);
//
//                    FileHandle fh = Gdx.files.local("radio_tmp_" + trackId + ".wav");
//                    fh.writeBytes(decodedBytes, false);
//
//                    GameAudioManager.getInstance().playMusic(fh, true, 1f);
//                }

                if ("radio-update".equalsIgnoreCase(message.getType())) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> b = (Map<String,Object>) message.getBody();
                    String trackId = (String) b.get("trackId");
                    String name    = (String) b.get("name");   // سرور هم اینو می‌فرسته
                    String base64  = (String) b.get("data");

                    if (base64 == null || base64.isEmpty()) return;

                    byte[] decodedBytes;
                    try {
                        decodedBytes = java.util.Base64.getDecoder().decode(base64);
                    } catch (IllegalArgumentException ex) {
                        System.err.println("[RADIO] base64 decode failed: " + ex.getMessage());
                        return;
                    }

                    // sniff header برای تشخیص فرمت
                    String ext = "bin";
                    try {
                        int headerLen = Math.min(decodedBytes.length, 12);
                        String header = new String(decodedBytes, 0, headerLen, java.nio.charset.StandardCharsets.US_ASCII);
                        if (header.startsWith("RIFF")) ext = "wav";
                        else if (header.startsWith("OggS")) ext = "ogg";
                        else if (header.startsWith("ID3") || (decodedBytes.length > 1 && (decodedBytes[0] & 0xFF) == 0xFF && ((decodedBytes[1] & 0xE0) == 0xE0))) ext = "mp3";
                        else if (name != null && name.lastIndexOf('.') > 0) {
                            ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
                        }
                    } catch (Exception ignored) {}

                    String fname = "radio_tmp_" + trackId + "." + ext;
                    FileHandle fh = Gdx.files.local(fname);
                    fh.writeBytes(decodedBytes, false);
                    System.out.println("[RADIO] wrote file: " + fh.file().getAbsolutePath() + " ext=" + ext + " size=" + fh.length());

                    // اگر WAV یا OGG بود، سعی کن پخش کنی (libGDX اغلب WAV/OGG را پشتیبانی می‌کند)
                    if ("wav".equals(ext) || "ogg".equals(ext)) {
                        try {
                            double durationSeconds = fh.length() / 176400.0;
                            boolean shouldLoop = durationSeconds > 2.0; // اگر کوتاه‌تر از 2 ثانیه، لوپ نکن

                            System.out.println("[RADIO] estimated duration (s): " + durationSeconds + " -> loop=" + shouldLoop);

                            GameAudioManager.getInstance().playMusic(fh, shouldLoop, 1f);
                            System.out.println("[RADIO] playback started for " + fname + " (loop=" + shouldLoop + ")");
                        } catch (GdxRuntimeException e) {
                            System.err.println("[RADIO] Failed to play audio: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else if ("mp3".equals(ext)) {
                        System.err.println("[RADIO] Received MP3. libGDX desktop OpenAL backend ممکن است MP3 را پشتیبانی نکند. بهتر است فایل را به WAV/OGG تبدیل کنید یا آپلود فقط WAV/OGG مجاز باشد.");
                    } else {
                        System.err.println("[RADIO] Unknown/unsupported audio format: " + ext);
                    }

                    return;
                }









                if("tile-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                String username = (String) bodyMap.get("username");
                                Object tileRaw = bodyMap.get("tile");
                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
                                int movingDirection = ((Number) bodyMap.get("movingDirection")).intValue();
                                boolean hasFainted = (Boolean) bodyMap.get("hasFainted");
                                for(User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()){
                                    if(otherPlayer.getUsername().equals(username)){
                                        otherPlayer.setCurrentTile(tile);
                                        otherPlayer.setMovingDirection(movingDirection);
                                        otherPlayer.setFainted(hasFainted);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("❌ Failed to parse updated user.");
                        }
                    });
                }
                if("plant-growable".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try{
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                int x = ((Number) bodyMap.get("x")).intValue();
                                int y = ((Number) bodyMap.get("y")).intValue();
                                boolean walkable = (Boolean) bodyMap.get("walkable");
                                boolean plowed = (Boolean) bodyMap.get("plowed");

//                               Object backpackRaw = bodyMap.get("inventory");
//                               Backpack backpack = GameSaver.convertObject(backpackRaw, Backpack.class);

                                Map<Item, Integer> inventory = convertToInventory((Map<String, Integer>) bodyMap.get("inventoryItems"));


                                Object growableRaw = bodyMap.get("containedGrowable");
                                Growable growable = GameSaver.convertObject(growableRaw, Growable.class);
                                String username = (String) bodyMap.get("username");
                                if(MainApp.getInstance().getCurrentGame().getCurrentPlayer().getUsername().equals(username)){
                                    MainApp.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().getInventoryItems().putAll(inventory);
                                }
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setWalkable(walkable);
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setIsPlowed(plowed);
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setContainedGrowable(growable);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            System.out.println("❌ Failed to parse updated plant.");
                        }
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

    public static Map<Item, Integer> convertToInventory(Map<String, ?> rawInventory) {
        Map<Item, Integer> result = new HashMap<>();
        for (Map.Entry<String, ?> entry : rawInventory.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            int amount;
            if (value instanceof Number) {
                amount = ((Number) value).intValue(); // works for both Integer and Double
            } else {
                continue; // or throw an error/log
            }

            Item item = Item.getRandomItem(name);
            if (item != null) {
                result.put(item, amount);
            } else {
                System.err.println("Unknown item: " + name);
            }
        }
        return result;
    }

//    public CompletableFuture<Message<?>> uploadTrack(String gameId, String name, byte[] raw) {
//        //String b64 = Arrays.toString(Base64Coder.encode(raw));
//        String b64 = new String(Base64Coder.encode(raw));
//        Map<String,Object> p = Map.of("ownerUsername", MainApp.getInstance().getLoggedInUser().getUsername(),
//            "name", name,
//            "data", b64);
//        return sendPost(gameId, "RadioController", "upload", p, MainApp.getInstance().getLoggedInUser().getUsername());
//    }
public CompletableFuture<Message<?>> uploadTrack(String gameId, String name, byte[] raw) {
    // use java.util.Base64 for consistent encoding
    String b64 = java.util.Base64.getEncoder().encodeToString(raw);

    System.out.println("[UPLOAD] raw bytes length = " + raw.length);
    System.out.println("[UPLOAD] base64 length = " + b64.length());

    Map<String,Object> p = Map.of(
        "ownerUsername", MainApp.getInstance().getLoggedInUser().getUsername(),
        "name", name,
        "data", b64
    );
    CompletableFuture<Message<?>> fut =
        sendPost(gameId, "RadioController", "upload", p, MainApp.getInstance().getLoggedInUser().getUsername());

    // optionally log when future completes
    fut.thenAccept(msg -> System.out.println("[UPLOAD] server response: " + msg.getStatus() + " / " + msg.getMessage()));
    return fut;
}

    public CompletableFuture<Message<?>> listTracks(String gameId) {
        Map<String,Object> p = Map.of("ownerUsername", MainApp.getInstance().getLoggedInUser().getUsername());
        return sendGet(gameId, "RadioController", "list", p, MainApp.getInstance().getLoggedInUser().getUsername());
    }
    public CompletableFuture<Message<?>> switchTrack(String gameId, String trackId) {
        Map<String,Object> p = Map.of("trackId", trackId);
        return sendPost(gameId, "RadioController", "switch", p, MainApp.getInstance().getLoggedInUser().getUsername());
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
