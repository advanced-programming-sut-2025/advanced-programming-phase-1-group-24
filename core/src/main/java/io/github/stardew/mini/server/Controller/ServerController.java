//package io.github.stardew.mini.server.Controller;
//
//import io.github.stardew.mini.Model.Message;
//import io.github.stardew.mini.Model.Result;
//import io.github.stardew.mini.Model.User;
//import io.github.stardew.mini.server.GameServer;
//import io.github.stardew.mini.server.LobbyManager;
//import io.github.stardew.mini.server.ServerApp;
//import io.javalin.http.Context;
//import org.eclipse.jetty.server.Server;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class ServerController {
//    private final GameController gameController = new GameController();
//    private final HouseMenuController houseMenuController = new HouseMenuController();
//    private final StoreMenuController storeMenuController = new StoreMenuController();
//    private final TradeMenuController tradeMenuController = new TradeMenuController();
//    private final NewGameMenuController newGameMenuController = new NewGameMenuController();
//    private final MapSelectionMenuController mapSelectionMenuController = new MapSelectionMenuController();
//    private final LobbyController lobbyController = new LobbyController();
//
//    public Message<?> routingTheRequests(Message<Map<String, Object>> message, GameServer server){
//        String controllerName = message.getControllerName();
//        String methodName = message.getMethodName();
//        Map<String, Object> body = message.getBody();
//        String username = message.getUsername();
//
//        if (controllerName == null || methodName == null || username == null) {
//            return Message.BAD_REQUEST.setMessage("Missing controllerName, methodName, or username");
//        }
//
//        User player = ServerApp.getInstance().getUserByUsername(username);
//        if (player == null) {
//            return Message.NOT_FOUND.setMessage("User not found: " + username);
//        }
//
//
//        switch (controllerName) {
//            case "GameController":
//                return routeToGameController(methodName, body, server, player);
//
//            case "HouseMenuController":
//                // TODO: Implement this
//                return Message.BAD_REQUEST.setMessage("HouseMenuController not implemented yet");
//
//            case "StoreMenuController":
//                // TODO: Implement this
//                return Message.BAD_REQUEST.setMessage("StoreMenuController not implemented yet");
//
//            case "TradeMenuController":
//                // TODO: Implement this
//                return Message.BAD_REQUEST.setMessage("TradeMenuController not implemented yet");
//            case "NewGameMenuController":
//                return routeToNewGameController(methodName, body, server, player);
//
//            case "MapSelectionMenuController":
//                return routeToMapSelectionMenuController(methodName, body, server, player);
//            case "LobbyController":
//                return  routeToLobbyController(methodName, body, server, player);
//
//
//            default:
//                return routeToGameController(methodName, body, server, player);
//        }
//    }
//
//
//    //We shouldn't always return ok
//
//    private Message<?> routeToGameController(String methodName, Map<String, Object> body, GameServer server, User player) {
//        Result result;
//
//        switch (methodName) {
//            case "tryMove" : {
//                String dx = (String) body.get("dx");
//                String dy = (String) body.get("dy");
//                String direction = (String) body.get("direction");
//                return gameController.tryMove(Integer.parseInt(dx), Integer.parseInt(dy), Integer.parseInt(direction), player, server);
//            }
//
//            case "useTool": {
//                String direction = (String) body.get("direction");
//                result = gameController.useTool(direction, player, server);
//                return Message.ok(result);
//            }
//
//            case "plantGrowable": {
//                String seedName = (String) body.get("seedName");
//                String direction = (String) body.get("direction");
//                result = gameController.plantGrowable(seedName, direction, player, server);
//                return Message.ok(result);
//            }
//
//            case "fertalizeGrowable": {
//                String fertalizer = (String) body.get("fertalizer");
//                String direction = (String) body.get("direction");
//                result = gameController.fertalizeGrowable(fertalizer, direction, player, server);
//                return Message.ok(result);
//            }
//
//            case "buildGreenHouse": {
//                result = gameController.buildGreenHouse(player, server);
//                return Message.ok(result);
//            }
//
//            default:
//                return Message.BAD_REQUEST.setMessage("Unknown method: " + methodName);
//        }
//    }
//
//    private Message<?> routeToNewGameController(String methodName, Map<String, Object> body, GameServer server, User player) {
//        Result result;
//        switch (methodName) {
//            case "createGameOnServer": {
//                System.out.println("called this");
//                Object usernamesRaw = body.get("usernames");
//                List<String> usernames = new ArrayList<>();
//
//                if (usernamesRaw instanceof List<?> list) {
//                    for (Object obj : list) {
//                        if (obj instanceof String str) {
//                            usernames.add(str);
//                        }
//                    }
//                }
//
//                // Optionally add the requesting user if not already present
//                if (!usernames.contains(player.getUsername())) {
//                    usernames.add(0, player.getUsername());
//                }
//
//                //return newGameMenuController.createGameOnServer(usernames); // <-- Updated
//                return newGameMenuController.createGameOnServer(usernames, player);
//            }
//            default:
//                return Message.BAD_REQUEST.setMessage("Unknown method: " + methodName);
//        }
//    }
//
//    private Message<?> routeToMapSelectionMenuController(String methodName, Map<String, Object> body, GameServer server, User player) {
//        switch (methodName) {
//            case "pickGameMap": {
//                Object mapNumberRaw = body.get("mapNumber");
//
//                if (mapNumberRaw == null || !(mapNumberRaw instanceof Number)) {
//                    return Message.BAD_REQUEST.setMessage("Invalid or missing mapNumber");
//                }
//
//                int mapNumber = ((Number) mapNumberRaw).intValue();
//
//                return mapSelectionMenuController.pickGameMap(player, mapNumber, server);
//            }
//
//            default:
//                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
//        }
//    }
//
//    private Message<?> routeToLobbyController(String methodName, Map<String, Object> body, GameServer server, User player) {
//        switch (methodName) {
//            case "createLobby": {
//                String lobbyName = (String) body.get("name");
//                String lobbyPassword = (String) body.get("password");
//                Boolean isPrivate = (Boolean) body.get("isPrivate");
//
//                lobbyController.createLobby(lobbyName, lobbyPassword, isPrivate, player);
//                return Message.OK.setMessage("lobby created");
//            }
//            case "getAllLobbies": {
//                return lobbyController.getAllLobbies();
//            }
//            case "joinLobby" : {
//                String lobbyID = (String) body.get("lobbyId");
//                String lobbyPassword = (String) body.get("password");
//                return lobbyController.joinLobby(lobbyID, lobbyPassword, player);
//            }
//            default:
//                return Message.NOT_FOUND.setMessage("Method not found: " + methodName);
//        }
//    }
//
//
////    public void routeToGameController(String methodName, Map<String, Object> body, Context ctx, GameServer server, User player) {
////        Result result = null;
////        switch (methodName) {
////            case "tryMove" :
////                //gameController.tryMove()
////                break;
////            case "exitGame":
////                break;
////            case "useTool":
////                String direction = (String) body.get("direction");
////                result = gameController.useTool(direction, player, server);
////                ctx.json(Message.ok(result));
////                break;
////            case "startForceTerminateVote":
////                break;
////            case "voteToTerminate":
////                break;
////            case "handleEndOfDay":
////                break;
////            case "cheatAdvanceDate":
////                break;
////            case "cheatAdvanceTime":
////                break;
////            case "cheatChangeWeather":
////                break;
////            case "cheatUnlimitedEnergy":
////                break;
////            case "cheatChangeEnergy":
////                break;
////            case "cheatThor":
////                break;
////            case "petAnimal":
////                break;
////            case "cheatAnimalFriendship":
////                break;
////            case "showOwnedAnimals":
////                break;
////            case "feedHay":
////                break;
////            case "shepherdAnimal":
////                break;
////            case "releaseAnimal":
////                break;
////            case "findShortestPath":
////                break;
////            case "getWalkableNeighbors":
////                break;
////            case "collectProduct":
////                break;
////            case "sellAnimal":
////                break;
////            case "walkTo":
////                break;
////            case "plantGrowable":
////                String seedName = (String) body.get("seedName");
////                direction = (String) body.get("direction");
////                result = gameController.plantGrowable(seedName, direction, player, server);
////                ctx.json(Message.ok(result));
////                break;
////            case "fertalizeGrowable":
////                String fertalizer = (String) body.get("fertalizer");
////                direction = (String) body.get("direction");
////                result = gameController.fertalizeGrowable(fertalizer, direction, player, server);
////                ctx.json(Message.ok(result));
////                break;
////            case "hug":
////                break;
////            case "askMarriage":
////                break;
////            case "respondToMarriage":
////                break;
////            case "cheatAddMoney":
////                break;
////            case "sendGift":
////                break;
////            case "rateGifts":
////                break;
////            case "sendFlower":
////                break;
////            case "cheatWalk":
////                break;
////            case "cheatSetSkill":
////                break;
////            case "cheatSetFriendshipLevel":
////                break;
////            case "cheatAddItem":
////                break;
////            case "artisanUse":
////                break;
////            case "showMoney":
////                break;
////            case "buildGreenHouse":
////                 result = gameController.buildGreenHouse(player, server);
////                 ctx.json(Message.ok(result));
////                break;
//////            case "plantGrowable":
//////                String seedName = (String) body.get("seedName");
//////                String direction = (String) body.get("direction");
//////                Result result = plantingController.plantGrowable(seedName, direction);
//////                ctx.json(new Message<>(200, "OK").setBody(result));
//////                break;
////        }
////    }
//
//}
package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.GameSummary;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.LobbyManager;
import io.github.stardew.mini.server.ServerApp;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Server;

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

    public Message<?> routingTheRequests(Message<Map<String, Object>> message, GameServer server){
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
                return routeToGameController(methodName, body, server, player);

            case "HouseMenuController":
                // TODO: Implement this
                return Message.BAD_REQUEST.setMessage("HouseMenuController not implemented yet");

            case "StoreMenuController":
                // TODO: Implement this
                return Message.BAD_REQUEST.setMessage("StoreMenuController not implemented yet");

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
//            case "SignupMenuController":
//                SignupMenuController signup = new SignupMenuController();
//                if ("signup".equals(methodName)) {
//                    return signup.signup(body);
//                } else if ("setSecurityQuestion".equals(methodName)) {
//                    return signup.setSecurityQuestion(body);
//                } else {
//                    return Message.NOT_FOUND.setMessage("Unknown method: " + methodName);
//                }



            default:
                return routeToGameController(methodName, body, server, player);
        }
    }


    //We shouldn't always return ok

    private Message<?> routeToGameController(String methodName, Map<String, Object> body, GameServer server, User player) {
        Result result;

        switch (methodName) {
            case "tryMove" : {
                String dx = (String) body.get("dx");
                String dy = (String) body.get("dy");
                String direction = (String) body.get("direction");
                return gameController.tryMove(Integer.parseInt(dx), Integer.parseInt(dy), Integer.parseInt(direction), player, server);
            }

            case "useTool": {
                String direction = (String) body.get("direction");
                result = gameController.useTool(direction, player, server);
                return Message.ok(result);
            }

            case "plantGrowable": {
                String seedName = (String) body.get("seedName");
                String direction = (String) body.get("direction");
                result = gameController.plantGrowable(seedName, direction, player, server);
                return Message.ok(result);
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
//    public void routeToGameController(String methodName, Map<String, Object> body, Context ctx, GameServer server, User player) {
//        Result result = null;
//        switch (methodName) {
//            case "tryMove" :
//                //gameController.tryMove()
//                break;
//            case "exitGame":
//                break;
//            case "useTool":
//                String direction = (String) body.get("direction");
//                result = gameController.useTool(direction, player, server);
//                ctx.json(Message.ok(result));
//                break;
//            case "startForceTerminateVote":
//                break;
//            case "voteToTerminate":
//                break;
//            case "handleEndOfDay":
//                break;
//            case "cheatAdvanceDate":
//                break;
//            case "cheatAdvanceTime":
//                break;
//            case "cheatChangeWeather":
//                break;
//            case "cheatUnlimitedEnergy":
//                break;
//            case "cheatChangeEnergy":
//                break;
//            case "cheatThor":
//                break;
//            case "petAnimal":
//                break;
//            case "cheatAnimalFriendship":
//                break;
//            case "showOwnedAnimals":
//                break;
//            case "feedHay":
//                break;
//            case "shepherdAnimal":
//                break;
//            case "releaseAnimal":
//                break;
//            case "findShortestPath":
//                break;
//            case "getWalkableNeighbors":
//                break;
//            case "collectProduct":
//                break;
//            case "sellAnimal":
//                break;
//            case "walkTo":
//                break;
//            case "plantGrowable":
//                String seedName = (String) body.get("seedName");
//                direction = (String) body.get("direction");
//                result = gameController.plantGrowable(seedName, direction, player, server);
//                ctx.json(Message.ok(result));
//                break;
//            case "fertalizeGrowable":
//                String fertalizer = (String) body.get("fertalizer");
//                direction = (String) body.get("direction");
//                result = gameController.fertalizeGrowable(fertalizer, direction, player, server);
//                ctx.json(Message.ok(result));
//                break;
//            case "hug":
//                break;
//            case "askMarriage":
//                break;
//            case "respondToMarriage":
//                break;
//            case "cheatAddMoney":
//                break;
//            case "sendGift":
//                break;
//            case "rateGifts":
//                break;
//            case "sendFlower":
//                break;
//            case "cheatWalk":
//                break;
//            case "cheatSetSkill":
//                break;
//            case "cheatSetFriendshipLevel":
//                break;
//            case "cheatAddItem":
//                break;
//            case "artisanUse":
//                break;
//            case "showMoney":
//                break;
//            case "buildGreenHouse":
//                 result = gameController.buildGreenHouse(player, server);
//                 ctx.json(Message.ok(result));
//                break;
////            case "plantGrowable":
////                String seedName = (String) body.get("seedName");
////                String direction = (String) body.get("direction");
////                Result result = plantingController.plantGrowable(seedName, direction);
////                ctx.json(new Message<>(200, "OK").setBody(result));
////                break;
//        }
//    }

}
