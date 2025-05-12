package org.example.Model.Friendships;

import org.example.Model.Things.Item;

public class Gift {
    private String sender;
    private String receiver;
    private Item item;
    private int amount;
    private int rate = 0;

    public Gift(){

    }

    public Gift(String from , String to , Item item , int amount) {
        this.sender = from;
        this.receiver = to;
        this.item = item;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
