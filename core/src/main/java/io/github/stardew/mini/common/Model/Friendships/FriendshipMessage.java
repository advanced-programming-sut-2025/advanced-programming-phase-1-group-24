package io.github.stardew.mini.common.Model.Friendships;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class FriendshipMessage {
    private String sender;
    private String recipient;
    private String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public FriendshipMessage() {
    }

    public FriendshipMessage(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
