package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.server.GameServer;
import io.javalin.http.Context;

import java.util.Map;

public class ServerController {
    private final GameController gameController = new GameController();
    private final HouseMenuController houseMenuController = new HouseMenuController();
    private final StoreMenuController storeMenuController = new StoreMenuController();
    private final TradeMenuController tradeMenuController = new TradeMenuController();

    public void routingTheRequests(Context ctx, GameServer server) {
        String controllerName = ctx.pathParam("controllerName");
        String methodName = ctx.pathParam("methodName");
        Message<Map<String, Object>> message = ctx.bodyAsClass(Message.class);
        Map<String, Object> body = message.getBody();

        switch (controllerName) {
            case "GameController":
                routeToGameController(methodName, body, ctx, server);
                break;

            case "HouseMenuController":
                break;
            case "StoreMenuController":

                break;
            case "TradeMenuController":
                break;

            default:
                ctx.json(Message.BAD_REQUEST.setMessage("Unknown controller: " + controllerName));
        }
    }

    public void routeToGameController(String methodName, Map<String, Object> body, Context ctx, GameServer server) {
        switch (methodName) {
//            case "plantGrowable":
//                String seedName = (String) body.get("seedName");
//                String direction = (String) body.get("direction");
//                Result result = plantingController.plantGrowable(seedName, direction);
//                ctx.json(new Message<>(200, "OK").setBody(result));
//                break;
        }
    }

}
