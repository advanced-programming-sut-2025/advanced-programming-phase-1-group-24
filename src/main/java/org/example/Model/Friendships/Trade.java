package org.example.Model.Friendships;

import org.example.Model.Things.Item;
import org.example.Model.User;

public class Trade {
    private User sender;
    private User recipient;
    private String typeOfTrade;
    private int amountOfOfferedItems;
    private String offeredItem;
    private String requestedItem;
    private int amountOfRequestedItem;
    private int requestedPrice;
    private boolean accept;
    private boolean hasBeenAnswered;
    private int id;
    private static int numberOfTrades = 0;

    public Trade(User sender, User recipient, String typeOfTrade, int amountOfOfferedItems, String offeredItem,
                 String requestedItem, int amountOfRequestedItem, int requestedPrice, boolean accept) {
        this.sender = sender;
        this.recipient = recipient;
        this.typeOfTrade = typeOfTrade;
        this.amountOfOfferedItems = amountOfOfferedItems;
        this.offeredItem = offeredItem;
        this.requestedItem = requestedItem;
        this.amountOfRequestedItem = amountOfRequestedItem;
        this.requestedPrice = requestedPrice;
        this.accept = accept;
        this.hasBeenAnswered = false;
        this.id = numberOfTrades;
        numberOfTrades++;
    }


    public User getSender() {
        return sender;
    }
    public User getRecipient() {
        return recipient;
    }
    public String getTypeOfTrade() {
        return typeOfTrade;
    }

    public int getAmountOfOfferedItems() {
        return amountOfOfferedItems;
    }

    public String getRequestedItem() {
        return requestedItem;
    }

    public String getOfferedItem() {
        return offeredItem;
    }

    public int getAmountOfRequestedItem() {
        return amountOfRequestedItem;
    }
    public int getRequestedPrice() {
        return requestedPrice;
    }
    public boolean isAccept() {
        return accept;
    }
    public int getId() {
        return id;
    }

    public boolean isHasBeenAnswered() {
        return hasBeenAnswered;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setAmountOfOfferedItems(int amountOfOfferedItems) {
        this.amountOfOfferedItems = amountOfOfferedItems;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setTypeOfTrade(String typeOfTrade) {
        this.typeOfTrade = typeOfTrade;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public void setOfferedItem(String offeredItem) {
        this.offeredItem = offeredItem;
    }

    public void setRequestedItem(String requestedItem) {
        this.requestedItem = requestedItem;
    }

    public void setAmountOfRequestedItem(int amountOfRequestedItem) {
        this.amountOfRequestedItem = amountOfRequestedItem;
    }

    public void setRequestedPrice(int requestedPrice) {
        this.requestedPrice = requestedPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHasBeenAnswered(boolean hasBeenAnswered) {
        this.hasBeenAnswered = hasBeenAnswered;
    }
}
