package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.GameSummary;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.NPCManagement.NPC;
import io.github.stardew.mini.Model.NPCManagement.NPCMission;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.LobbyManager;
import io.github.stardew.mini.server.ServerApp;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Server;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerController {
    private final GameController gameController = new GameController();
    private final HouseMenuController houseMenuController = new HouseMenuController();
    private final StoreMenuController storeMenuController = new StoreMenuController();
    private final TradeMenuController tradeMenuController = new TradeMenuController();
    private final NewGameMenuController newGameMenuController = new NewGameMenuController();
    private final MapSelectionMenuController mapSelectionMenuController = new MapSelectionMenuController();
    private final PreGameMenuController preGameMenuController = new PreGameMenuController();
    private final LobbyController lobbyController = new LobbyController();
    private final MainMenuController mainMenuController= new MainMenuController();

    public Message<?> routingTheRequests(Message<Map<String, Object>> message, GameServer server) throws Exception{
        String controllerName = message.getControllerName();
        String methodName = message.getMethodName();
        Map<String, Object> body = message.getBody();
        String username = message.getUsername();

        if (controllerName == null || methodName == null || username == null) {
            return Message.BAD_REQUEST.setMessage("Missing controllerName, methodName, or username");
        }

        if (controllerName.equals("SignupMenuController")) {
            SignupMenuController signup = new SignupMenuController();
            if ("signup".equals(methodName)) {
                return signup.signup(body);
            } else if ("setSecurityQuestion".equals(methodName)) {
                return signup.setSecurityQuestion(body);
            } else {
                return Message.NOT_FOUND.setMessage("Unknown method: " + methodName);
            }
        }

        User player = ServerApp.getInstance().getUserByUsername(username);
        if (player == null) {
            return Message.NOT_FOUND.setMessage("User not found: " + username);
        }


        switch (controllerName) {
            case "GameController":
                return routeToGameController(methodName, body, server, player, message);

            case "HouseMenuController":
                // TODO: Implement this
                return Message.BAD_REQUEST.setMessage("HouseMenuController not implemented yet");

            case "StoreMenuController":
                System.out.println("routing to store menu controller ");
                return routeToStoreMenuController(methodName, body, server, player);

            case "TradeMenuController":
                // TODO: Implement this
                return Message.BAD_REQUEST.setMessage("TradeMenuController not implemented yet");
            case "NewGameMenuController":
                return routeToNewGameController(methodName, body, server, player);

            case "MapSelectionMenuController":
                return routeToMapSelectionMenuController(methodName, body, server, player);
            case "LobbyController":
                return  routeToLobbyController(methodName, body, server, player);
            case "PreGameMenuController":
                return  routeToPreGameMenuController(methodName, body, server, player);
            case "MainMenuController":
                return routeToMainMenuController(methodName, body, player);
            case "RadioController":
                return RadioController.route(methodName, body, server);


            default:
                return routeToGameController(methodName, body, server, player, message);
        }
    }


    //We shouldn't always return ok

    private Message<?> routeToGameController(String methodName, Map<String, Object> body, GameServer server, User player, Message<Map<String, Object>> fullMessage) throws Exception {
        Result result;

        switch (methodName) {
            case "tryMove" : {
                String dx = (String) body.get("dx");
                String dy = (String) body.get("dy");
                String direction = (String) body.get("direction");
                return gameController.tryMove(Integer.parseInt(dx), Integer.parseInt(dy), Integer.parseInt(direction), player, server);
            }

            case "plantGrowable": {
                String seedName = (String) body.get("seedName");
                String direction = (String) body.get("direction");
                return gameController.plantGrowable(seedName, direction, player, server);
            }

            case "fertalizeGrowable": {
                String fertalizer = (String) body.get("fertalizer");
                String direction = (String) body.get("direction");
                result = gameController.fertalizeGrowable(fertalizer, direction, player, server);
                return Message.ok(result);
            }

            case "buildGreenHouse": {
                result = gameController.buildGreenHouse(player, server);
                return Message.ok(result);
            }
            case "exitGame": {
                result = gameController.exitGame(player, server);
                return Message.ok(result);
            }
            case "getSavedGames": {
                List<GameSummary> summaries = gameController.getSavedGamesForUser(player.getUsername());
                return Message.ok(summaries);
            }
            case "cheatAddMoney": {
                String count = (String) body.get("money");
                result = gameController.cheatAddMoney(count, player, server);
                return Message.ok(result).setMessage(result.getMessage());
            }
            case "cheatAddItem": {
                System.out.println("going to cheat add item");
                String count = (String) body.get("count");
                String itemName = (String) body.get("itemName");
                result = gameController.cheatAddItem(itemName,count, player, server);
                return Message.ok(result).setMessage(result.getMessage());

            }
            case "getLeaderboard": {
                // اینجا game server رو پاس بده
                return gameController.getLeaderboard(server);
            }
//            case "getLeaderboard": {
//                List<Map<String, Object>> lb = server.broadcastLeaderboard();
//                // مستقیم به کلاینت می‌فرستیم
//                return Message.ok(lb);
//            }
            case "handleChatMessage": {
                String senderUsername = fullMessage.getUsername();
                String gameId = fullMessage.getGameID();
                return gameController.handleChatMessage(senderUsername, gameId, body, server); // Modified call
            }

            case "handleReaction": {
                return gameController.handleReaction(player, server, body);
            }

            case "startForceTerminateVote": {
                return gameController.startForceTerminateVote(player, server);
            }
            case "voteToTerminate": {
                Boolean approve = (Boolean) body.get("approve");
                if (approve == null) {
                    return Message.BAD_REQUEST.setMessage("Missing 'approve' parameter for vote.");
                }
                return gameController.voteToTerminate(player, server, approve);
            }

            case "startVoteOut": {
                String targetUsername = (String) body.get("target");
                if (targetUsername == null) {
                    return Message.BAD_REQUEST.setMessage("Missing 'target' username for vote-out.");
                }
                return gameController.startVoteOut(player, targetUsername, server);
            }
            case "castVoteOut": {
                Boolean approve = (Boolean) body.get("approve");
                if (approve == null) {
                    return Message.BAD_REQUEST.setMessage("Missing 'approve' parameter for vote.");
                }
                return gameController.castVoteOut(player, approve, server);
            }

            case "useTool": {
                String direction = (String) body.get("direction");
                String toolName = (String) body.get("toolName");
                if (direction == null || toolName == null) {
                    return Message.BAD_REQUEST.setMessage("Missing direction or toolName.");
                }
                Result toolResult = gameController.useToolServer(direction, player, server, toolName);
                if (toolResult.isSuccessful()) {
                    return Message.OK.setMessage(toolResult.message());
                } else {
                    return Message.FORBIDDEN.setMessage(toolResult.message());
                }
            }

            case "addCaughtFish": {
                Message<?> fishResult = gameController.addCaughtFish(player, server, body);
                return fishResult;
            }

            case "talk": {
                return gameController.talk(player, server, body);
            }
            case "hug": {
                return gameController.hug(player, server, body);
            }
            case "sendGift": {
                return gameController.sendGift(player, server, body);
            }
            case "sendFlower": {
                return gameController.sendFlower(player, server, body);
            }
            case "askMarriage": {
                return gameController.askMarriage(player, server, body);
            }
            case "respondToMarriage": {
                return gameController.respondToMarriage(player, server, body);
            }
            case "cheatSetFriendshipLevel": {
                return gameController.cheatSetFriendshipLevel(player, server, body);
            }

            case "updateNpcPosition": {
                String npcName = (String) body.get("npcName");
                Point currentPoint = GameSaver.convertObject(body.get("currentPoint"), Point.class);
                Point movingTo = GameSaver.convertObject(body.get("movingTo"), Point.class);
                Point movingFrom = GameSaver.convertObject(body.get("movingFrom"), Point.class);

                result = gameController.updateNpcPosition(npcName, currentPoint, movingTo, movingFrom, server);
                return Message.ok(result);
            }
            case "doNPCMission": {
                String mission = (String) body.get("mission");
                String currentPlayer = (String) body.get("currentPlayer");
                Result resulttt = gameController.doMission(mission, currentPlayer);
                return Message.ok(resulttt);
            }
            case "addNPCMission": {
                String mission = (String) body.get("mission");
                String currentPlayer = (String) body.get("currentPlayer");
                Result resultttt = gameController.addNPCMission(mission,currentPlayer);
                return Message.ok(resultttt);
            }

            case "initiateTrade": {
                return gameController.initiateTrade(player, server, body);
            }
            case "respondToTrade": {
                return gameController.respondToTrade(player, server, body);
            }
            case "updateTradeOffer": {
                return gameController.updateTradeOffer(player, server, body);
            }
            case "confirmTradeOffer": {
                return gameController.confirmTradeOffer(player, server, body);
            }
            case "finalizeTrade": {
                return gameController.finalizeTrade(player, server, body);
            }
            case "cancelTrade": {
                return gameController.cancelTrade(player, server, body);
            }
            case "getTradeHistory": {
                return gameController.getTradeHistory(player, server);
            }


            default:
                return Message.BAD_REQUEST.setMessage("Unknown method: " + methodName);
        }
    }

    private Message<?> routeToNewGameController(String methodName, Map<String, Object> body, GameServer server, User player) {
        Result result;
        switch (methodName) {
            case "createGameOnServer": {
                System.out.println("called this");
                Object usernamesRaw = body.get("usernames");
                List<String> usernames = new ArrayList<>();

                if (usernamesRaw instanceof List<?> list) {
                    for (Object obj : list) {
                        if (obj instanceof String str) {
                            usernames.add(str);
                        }
                    }
                }

                // Optionally add the requesting user if not already present
                if (!usernames.contains(player.getUsername())) {
                    usernames.add(0, player.getUsername());
                }

                //return newGameMenuController.createGameOnServer(usernames); // <-- Updated
                return newGameMenuController.createGameOnServer(usernames, player);
            }
            default:
                return Message.BAD_REQUEST.setMessage("Unknown method: " + methodName);
        }
    }

    private Message<?> routeToMapSelectionMenuController(String methodName, Map<String, Object> body, GameServer server, User player) {
        switch (methodName) {
            case "pickGameMap": {
                Object mapNumberRaw = body.get("mapNumber");

                if (mapNumberRaw == null || !(mapNumberRaw instanceof Number)) {
                    return Message.BAD_REQUEST.setMessage("Invalid or missing mapNumber");
                }

                int mapNumber = ((Number) mapNumberRaw).intValue();

                return mapSelectionMenuController.pickGameMap(player, mapNumber, server);
            }

            default:
                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
        }
    }

    private Message<?> routeToLobbyController(String methodName, Map<String, Object> body, GameServer server, User player) {
        switch (methodName) {
            case "createLobby": {
                String lobbyName = (String) body.get("name");
                String lobbyPassword = (String) body.get("password");
                Boolean isPrivate = (Boolean) body.get("isPrivate");
                Boolean isInvisible = (Boolean) body.get("isInvisible");

                lobbyController.createLobby(lobbyName, lobbyPassword, isPrivate, isInvisible, player);
                return Message.OK.setMessage("lobby created");
            }
            case "getAllLobbies": {
                return lobbyController.getAllLobbies();
            }
            case "joinLobby" : {
                String lobbyID = (String) body.get("lobbyId");
                String lobbyPassword = (String) body.get("password");
                return lobbyController.joinLobby(lobbyID, lobbyPassword, player);
            }
            case "searchLobbyById": {
                String lobbyID = (String) body.get("lobbyID");
                return lobbyController.searchLobbyById(lobbyID);
            }
            case "leaveLobby": {
                String lobbyID = (String) body.get("lobbyId");
                return lobbyController.leaveLobby(lobbyID, player);
            }
            default:
                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
        }
    }

    private Message<?> routeToPreGameMenuController(String methodName, Map<String, Object> body, GameServer server, User player) {
        switch (methodName) {
            case "loadGame": {
                String gameId = (String) body.get("gameId");

                preGameMenuController.loadGame(player,gameId);
                return Message.OK.setMessage("gameLoaded");
            }


            default:
                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
        }
    }
    private Message<?> routeToMainMenuController(String methodName, Map<String, Object> body, User player) {
        switch (methodName) {
            case "showUserInfo": {
               Result result= mainMenuController.showUserInfo(player.getUsername());
                return Message.OK.setMessage(result.getMessage());
            }


            default:
                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
        }
    }
    private Message<?> routeToStoreMenuController(String methodName, Map<String, Object> body, GameServer server, User player) {
        if ("purchase".equalsIgnoreCase(methodName)) {
            return storeMenuController.purchase(server, player, body);
        }
        if ("buyAnimal".equalsIgnoreCase(methodName)) {
            return storeMenuController.buyAnimal(server, player, body);
        }
        if ("buyFromCarpenter".equalsIgnoreCase(methodName)) {
            return storeMenuController.buyFromCarpenter(server, player, body);
        }
        if ("upgradeTool".equalsIgnoreCase(methodName)) {
            return storeMenuController.upgradeTool(server, player, body);
        }
        return Message.NOT_FOUND.setMessage("Unknown method in StoreMenuController: " + methodName);
    }



}
