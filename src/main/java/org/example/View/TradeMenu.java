package org.example.View;


import org.example.Controller.TradeMenuController;
import org.example.Model.Menus.TradeMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class TradeMenu implements AppMenu {
    TradeMenuController tradeController = new TradeMenuController();
    public void handleCommand(Scanner scanner){
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = TradeMenuCommands.OFFER_TRADE.getMatcher(input)) != null) {
            System.out.println(tradeController.offerTrade(matcher.group("username"), matcher.group("type"), matcher.group("item"),
                    Integer.parseInt(matcher.group("amount")), Integer.parseInt(matcher.group("price"))).message());
        }
        else if((matcher = TradeMenuCommands.REQUEST_OFFER.getMatcher(input)) != null) {
            System.out.println(tradeController.requestTrade(matcher.group("username"), matcher.group("type"),matcher.group("item"),
                    Integer.parseInt(matcher.group("amount")), matcher.group("targetItem"),
                    Integer.parseInt(matcher.group("targetAmount"))).message());

        }
        else if((matcher = TradeMenuCommands.TRADE_RESPONSE.getMatcher(input)) != null) {
            System.out.println(tradeController.tradeResponse(matcher.group("respond"), Integer.parseInt(matcher.group("id"))).message());
        }
        else if((matcher = TradeMenuCommands.TRADE_HISTORY.getMatcher(input)) != null) {
            System.out.println(tradeController.showTradeHistory().message());
        }
        else if((matcher = TradeMenuCommands.TRACE_LIST.getMatcher(input)) != null) {
            System.out.println(tradeController.showTradeList().message());
        }
        else if((matcher = TradeMenuCommands.EXIT_TRADE.getMatcher(input)) != null) {
            tradeController.exitTrade();
        }
        else {
            System.out.println("Invalid command");
        }
    }

}
