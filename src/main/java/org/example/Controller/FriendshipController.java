package org.example.Controller;

import org.example.Model.App;
import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Gift;
import org.example.Model.Friendships.Message;
import org.example.Model.Game;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Menus.Menu;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;
import org.example.Model.Result;
import org.example.Model.Things.Item;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendshipController {


    public Result sendGift(String receiverUsername, String item, String amountString) {
        Game game = App.getInstance().getCurrentGame();
          int  amount = Integer.parseInt(amountString);


        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(receiverUsername);
        Friendship friendship = game.getFriendship(senderUsername, receiverUsername);
        if (sender == null || receiver == null || friendship == null) {
            return new Result(false, "Sender or receiver or friendship not found.");
        }
        if (friendship.getLevel() < 1) {
            return new Result(false, "You don't have enough level!");
        }

        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile())) {
            return new Result(false, "Players are not adjacent.");
        }

        int availableCount = sender.getBackpack().getItemCount(item);
        if (availableCount < amount) {
            return new Result(false, "Sender does not have enough of the specified item.");
        }

        Item itemToGift = sender.getBackpack().grabItemAndReturn(item, amount);
        if (itemToGift == null) {
            return new Result(false, "Sender does not have enough of the specified item.");
        }

        // Remove from sender and add to receiver
        Result result = receiver.getBackpack().addItem(itemToGift.copy(), amount);
        if (!result.isSuccessful()) {
            return new Result(false, "Receiver inventory is full!");
        }
        sender.getBackpack().grabItem(item, amount);

        // Log the gift in friendship
        Gift gift = new Gift(senderUsername, receiverUsername, itemToGift, amount);
        friendship.addToGifts(gift);

        // Notify receiver to rate the gift
        receiver.addToNotifications(new Message(senderUsername, receiverUsername, "You received " + amount + " of " + itemToGift.getName()));
        receiver.addRecievedGift(gift);

        return new Result(true, "Gift sent successfully. Waiting for receiver to rate the gift.");
    }


    public Result rateGifts(String giftNumberString, String rateString) {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();
        // Retrieve the received gift
        List<Gift> receivedGifts = currentPlayer.getRecievedGift();

        // Convert gift number and rating to appropriate types
        int giftNumber = Integer.parseInt(giftNumberString);
        int rating = Integer.parseInt(rateString);

        if (receivedGifts == null || receivedGifts.isEmpty()) {
            return new Result(false, "No gifts received yet.");
        }

        // Validate rating range
        if (rating < 1 || rating > 5) {
            return new Result(false, "Rating must be between 1 and 5.");
        }


        if (giftNumber < 1 || giftNumber > receivedGifts.size()) {
            return new Result(false, "No gift found with the specified gift number.");
        }

        Gift gift = receivedGifts.get(giftNumber - 1); // Gift number is 1-based

        // Apply the rating to the friendship
        User sender = game.getPlayerByUsername(gift.getSender());
        Friendship friendship = game.getFriendship(currentPlayer.getUsername(), sender.getUsername());

        if (friendship == null) {
            return new Result(false, "No friendship exists between you and the sender.");
        }
        if (friendship.getLevel() < 1) {
            return new Result(false, "You don't have enough level!");
        }

        // Calculate the friendship level change based on the formula
        int friendshipChange = 15 + 30 * (rating - 3);
        if (currentPlayer.getPartner() != null && currentPlayer.getPartner().getUsername().equals(gift.getSender())) {
            friendship.addXp(50);
        } else {
            friendship.addXp(friendshipChange);
        }

        // Log the rating
        gift.setRate(rating);  // Assuming Gift has a setRating method

        return new Result(true, "Gift rated successfully. Friendship level adjusted.");
    }



    public Result sendFlower(String receiverUsername) {
        Game game = App.getInstance().getCurrentGame();
        User sender = game.getCurrentPlayer();
        String senderUsername = sender.getUsername();
        User receiver = game.getPlayerByUsername(receiverUsername);
        if (receiver == null) {
            return new Result(false, "Receiver not found.");
        }

        Friendship friendship = game.getFriendship(senderUsername, receiverUsername);
        if (friendship == null) {
            return new Result(false, "friendship not found.");

        }
        if (friendship.getLevel() < 2) {
            return new Result(false, "You don't have enough level!");
        }

        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile())) {
            return new Result(false, "Players are not adjacent.");
        }

        final String FLOWER_ITEM_NAME = "Bouquet";
        int flowerCount = sender.getBackpack().getItemCount(FLOWER_ITEM_NAME);
        if (flowerCount < 1) {
            return new Result(false, "You don't have a flower to send.");
        }

        // Transfer 1 flower
        Item flower = sender.getBackpack().grabItemAndReturn(FLOWER_ITEM_NAME, 1);
        if (flower == null) {
            return new Result(false, "Failed to grab flower from inventory.");
        }

        Result addResult = receiver.getBackpack().addItem(flower.copy(), 1);
        if (!addResult.isSuccessful()) {
            return new Result(false, "Receiver's inventory is full.");
        }

        sender.getBackpack().grabItem(FLOWER_ITEM_NAME, 1);
        String reez = receiver.isGender() ? "pretty" : "handsome";
        receiver.addToNotifications(new Message(senderUsername, receiverUsername, "Here is a flower " + reez + " :)"));
        // Friendship level logic
        if (friendship.getLevel() == 2) {
            friendship.setLevel(3);
        }

        return new Result(true, "Flower sent successfully.");
    }
    public Result talk(String username, String message) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null) {
            return new Result(false, "One or both users of the relation not found.");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        if (sender.getPartner() != null && sender.getPartner().equals(receiver.getPartner())) {
            friendship.addXp(50);
        } else friendship.addXp(20);
        receiver.addToNotifications(new Message(senderUsername, receiver.getUsername(), senderUsername + "sent you a message :" + message));
        friendship.getTalkHistory().add(new Message(sender.getUsername(), receiver.getUsername(), message));
        return new Result(true, "message sent successfully to " + receiver.getUsername());
    }

    public Result showTalkHistory(String username) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Friendship friendship = game.getFriendship(player.getUsername(), username);
        ArrayList<Message> relevantMessages = new ArrayList<>();

        StringBuilder historyBuilder = new StringBuilder();
        ArrayList<Message> talkHistory = friendship.getTalkHistory();

        if (talkHistory.isEmpty()) {
            return new Result(false, "No messages found between you and " + username);
        }

        historyBuilder.append("Talk history with ").append(username).append(":\n");

        for (Message msg : talkHistory) {
            String direction = msg.getSender().equals(player.getUsername()) ? "You" : msg.getSender();
            String recipient = msg.getRecipient().equals(player.getUsername()) ? "You" : msg.getRecipient();

            historyBuilder
                    .append("- From ").append(direction)
                    .append(" to ").append(recipient)
                    .append(": ").append(msg.getMessage())
                    .append("\n");
        }

        return new Result(true, historyBuilder.toString());
    }

    public Result hug(String username) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null)
            return new Result(false, "One or both users of the relation not found.");
        if (friendship.getLevel() < 2) {
            return new Result(false, "You have not enough level!");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        if (sender.getPartner() != null && sender.getPartner().equals(receiver.getPartner())) {
            friendship.addXp(50);
        } else friendship.addXp(60);
        return new Result(true, "You succesfully hugged " + username + "(ah che chendeshi)");
    }

    public Result askMarriage(String username, String ring) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null)
            return new Result(false, "One or both users ot the relation not found.");
        if (sender.isGender())
            return new Result(false, "Only the male players can ask marriage.");
        if (friendship.getLevel() < 3) {
            return new Result(false, "You have not enough level!");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        Item item = sender.getBackpack().grabItemAndReturn("Wedding Ring", 1);
        if (item == null)
            return new Result(false, "You don't have any ring!");
        receiver.addToNotifications(new Message(senderUsername, receiver.getUsername(), senderUsername + "has asked to marry you"));
        return new Result(true, "You're marriage request has been sent successfully to " + receiver.getUsername());
    }

    public Result respondToMarriage(String response, String username) {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(currentPlayer.getUsername(), username);
        if (response.equals("accept")) {
            randomStuff ring = (randomStuff) receiver.getBackpack().grabItemAndReturn("Wedding Ring", 1);
            currentPlayer.getBackpack().addItem(ring, 1);
            friendship.setLevel(4);
            currentPlayer.setPartner(receiver);
            receiver.setPartner(currentPlayer);
            int sharedMoney = currentPlayer.getMoney() + receiver.getMoney();
            currentPlayer.setMoney(sharedMoney);
            receiver.setMoney(sharedMoney);
            return new Result(true, "You are married now. Ishalla mobarakesh bad!");
        } else {
            friendship.setLevel(0);
            receiver.setEnergy(receiver.getEnergy() / 2);
            receiver.setDaysSinceRejection(7);
            return new Result(false, "fekr kardi pool dari ya ghiafe!");
        }
    }


    public boolean isAdjacent(Tile t1, Tile t2) {
        int dx = Math.abs(t1.getX() - t2.getX());
        int dy = Math.abs(t1.getY() - t2.getY());
        return dx <= 1 && dy <= 1;
        //&& !(dx == 0 && dy == 0); // Exclude the same tile
    }
}