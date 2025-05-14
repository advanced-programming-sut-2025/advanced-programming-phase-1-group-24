package org.example.Controller;

import org.example.Model.App;
import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Message;
import org.example.Model.Friendships.Trade;
import org.example.Model.Menus.Menu;
import org.example.Model.Result;
import org.example.Model.Things.Backpack;
import org.example.Model.User;

import java.util.ArrayList;

public class TradeMenuController implements MenuController {
    public Result offerTrade(String username, String type,String item, int amount, int price) {
        User user = App.getInstance().getUserByUsername(username);
        if (user == null) {
            return new Result(false, "There is no user with the given username.");
        }
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Backpack playerBackpack = player.getBackpack();
        if(!type.equalsIgnoreCase("offer")) {
            return new Result(false, "This is not the right trade type.");
        }
        if(amount == 0) {
            return new Result(false, "Wrong amount format!");
        }
        if(!playerBackpack.hasItem(item, amount)) {
            return new Result(false, "You don't have enough items.");
        }
        Trade trade = new Trade(player, user, type, amount, item, null, 0, price, false);
        user.getTradingHistory().add(trade);
        String messageText = String.format(
                "You have received a trade offer from %s!\n" +
                        "Trade ID: %d\n" +
                        "Offered Item: %d x %s\n" +
                        "Requested Price: %d coins\n",
                player.getUsername(), trade.getId(),
                trade.getAmountOfOfferedItems(), trade.getOfferedItem(),
                trade.getRequestedPrice()
        );
        user.getTradeNotifications().add(new Message(player.getUsername(), user.getUsername(), messageText));
        return new Result(true, "Trade has been successfully placed!");
    }

    public Result requestTrade(String username, String type,String item, int amount, String targetItem, int targetAmount) {
        User user = App.getInstance().getUserByUsername(username);
        if (user == null) {
            return new Result(false, "There is no user with the given username.");
        }
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Backpack playerBackpack = player.getBackpack();
        if(!type.equalsIgnoreCase("request")) {
            return new Result(false, "This is not the right trade type.");
        }
        if(amount == 0) {
            return new Result(false, "Wrong amount format!");
        }
        if(!playerBackpack.hasItem(item, amount)) {
            return new Result(false, "You don't have enough items.");
        }
        Trade trade = new Trade(player, user, type, amount, item, targetItem, targetAmount, 0, false);
        user.getTradingHistory().add(trade);
        String messageText = String.format(
                "You have received a trade request from %s!\n" +
                        "Trade ID: %d\n" +
                        "They offer: %d x %s\n" +
                        "In exchange for: %d x %s",
                player.getUsername(), trade.getId(),
                trade.getAmountOfOfferedItems(), trade.getOfferedItem(),
                trade.getAmountOfRequestedItem(), trade.getRequestedItem()
        );
        user.getTradeNotifications().add(new Message(player.getUsername(), user.getUsername(), messageText));
        return new Result(true, "Trade has been successfully placed!");
    }

    public Result showTradeList() {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        ArrayList<Trade> trades = currentPlayer.getTradingHistory();

        if (trades.isEmpty()) {
            return new Result(true, "You have no trades in your history.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Your trade history:\n");

        for (Trade trade : trades) {
            if (!trade.isHasBeenAnswered()){
                sb.append("====================================\n");
            sb.append("Trade ID: ").append(trade.getId()).append("\n");
            sb.append("Sender: ").append(trade.getSender().getUsername()).append("\n");
            sb.append("Recipient: ").append(trade.getRecipient().getUsername()).append("\n");
            sb.append("Trade Type: ").append(trade.getTypeOfTrade()).append("\n");
            sb.append("Offered: ").append(trade.getAmountOfOfferedItems()).append(" x ").append(trade.getOfferedItem()).append("\n");

            if (trade.getTypeOfTrade().equalsIgnoreCase("request")) {
                sb.append("Requested: ").append(trade.getAmountOfRequestedItem()).append(" x ").append(trade.getRequestedItem()).append("\n");
            } else if (trade.getTypeOfTrade().equalsIgnoreCase("offer")) {
                sb.append("Requested Price: ").append(trade.getRequestedPrice()).append(" coins\n");
            }
            }
        }

        return new Result(true, sb.toString());
    }


    public Result tradeResponse(String response, int id){
        Trade trade = findTradeByID(id);
        if(trade == null){
            return new Result(false, "There is no trade with the given ID.");
        }
        User sender = trade.getSender();
        User receiver = trade.getRecipient();
        Friendship friendship = App.getInstance().getCurrentGame().getFriendship(sender.getUsername(), receiver.getUsername());
        trade.setHasBeenAnswered(true);
        if(response.equals("accept")) {
           friendship.addXp(50);
           trade.setAccept(true);
           if(trade.getTypeOfTrade().equalsIgnoreCase("request")){
               if(!receiver.getBackpack().hasItem(trade.getRequestedItem(), trade.getAmountOfRequestedItem())) {
                   return new Result(false, "You don't have enough items.");
               }
               receiver.getBackpack().grabItem(trade.getRequestedItem(), trade.getAmountOfRequestedItem());
               sender.getBackpack().grabItem(trade.getOfferedItem(), trade.getAmountOfOfferedItems());
           }
           else if(trade.getTypeOfTrade().equalsIgnoreCase("offer")){
               if(receiver.getMoney() < trade.getRequestedPrice()) {
                   return new Result(false, "You don't have enough money.");
               }
               receiver.decreaseMoney(trade.getRequestedPrice());
               sender.getBackpack().grabItem(trade.getOfferedItem(), trade.getAmountOfOfferedItems());
           }
           return new Result(true, "Trade has been successfully placed!");
        }
        else if(response.equals("reject")) {
            friendship.subtractXp(30);
            trade.setAccept(false);
            return new Result(false, "Trade has been successfully rejected.");
        }
        else{
            return new Result(false, "Invalid response.");
        }
    }

    public Result showTradeHistory(){
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        ArrayList<Trade> trades = currentPlayer.getTradingHistory();

        if (trades.isEmpty()) {
            return new Result(true, "You have no trades in your history.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Your trade history:\n");

        for (Trade trade : trades) {
            sb.append("====================================\n");
            sb.append("Trade ID: ").append(trade.getId()).append("\n");
            sb.append("Sender: ").append(trade.getSender().getUsername()).append("\n");
            sb.append("Recipient: ").append(trade.getRecipient().getUsername()).append("\n");
            sb.append("Trade Type: ").append(trade.getTypeOfTrade()).append("\n");
            sb.append("Offered: ").append(trade.getAmountOfOfferedItems()).append(" x ").append(trade.getOfferedItem()).append("\n");

            if (trade.getTypeOfTrade().equalsIgnoreCase("request")) {
                sb.append("Requested: ").append(trade.getAmountOfRequestedItem()).append(" x ").append(trade.getRequestedItem()).append("\n");
            } else if (trade.getTypeOfTrade().equalsIgnoreCase("offer")) {
                sb.append("Requested Price: ").append(trade.getRequestedPrice()).append(" coins\n");
            }

            sb.append("Accepted: ").append(trade.isAccept() ? "Yes" : "No").append("\n");
            sb.append("Answered: ").append(trade.isHasBeenAnswered() ? "Yes" : "No").append("\n");
        }

        return new Result(true, sb.toString());
    }

    public void exitTrade(){
        App.getInstance().setCurrentMenu(Menu.GameMenu);
    }

    public Trade findTradeByID(int id) {
        ArrayList<Trade> trades = App.getInstance().getCurrentGame().getCurrentPlayer().getTradingHistory();
        for (Trade trade : trades) {
            if (trade.getId() == id) {
                return trade;
            }
        }
        return null;
    }

}
