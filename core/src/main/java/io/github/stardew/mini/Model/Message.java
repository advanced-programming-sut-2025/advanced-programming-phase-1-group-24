package io.github.stardew.mini.Model;


public class Message<T> {
    public static final Message<?> UNAUTHORIZED = new Message<>(401, "Unauthorized", null, MessageType.RESPONSE);
    public static final Message<?> FORBIDDEN = new Message<>(403, "Forbidden", null, MessageType.RESPONSE);
    public static final Message<?> NOT_FOUND = new Message<>(404, "Not Found", null, MessageType.RESPONSE);
    public static final Message<?> INTERNAL_SERVER_ERROR = new Message<>(500, "Internal Server Error", null, MessageType.RESPONSE);
    public static final Message<?> BAD_REQUEST = new Message<>(400);
    public static final Message<?> OK = new Message<>(200, "OK", null, MessageType.RESPONSE);

    public static final String CHAT_PUBLIC = "chat_public";
    public static final String CHAT_PRIVATE = "chat_private";
    public static final String POP_UP_NOTIFICATION = "pop_up_notification";
    public static final String REACTION_BROADCAST = "reaction-broadcast";
    public static final String PLAYER_INTERACTION_BROADCAST = "player_interaction_broadcast";
    public static final String MARRIAGE_PROPOSAL = "marriage_proposal";
    public static final String FRIENDSHIP_UPDATED = "friendship_updated";
    public static final String TILE_UPDATE = "tile-update";
    public static final String SKILL_UPDATE = "skill-update";
    public static final String GIFT_SENT_UPDATE = "GIFT_SENT_UPDATE";
    public static final String MARRIAGE_RESPONSE_UPDATE = "MARRIAGE_RESPONSE_UPDATE";

    private int status;
    private String message;
    private T body;
    private long timestamp;
    private MessageType messageType;
    private String username; // optional, used in handshake
    private String type;  // optional, e.g. "connect", "move", etc.
    private String controllerName;
    private String methodName;
    private String requestId;
    private String gameID;
    private String token;

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public enum MessageType {
        REQUEST, RESPONSE
    }



    public static Message success(String message) {
        return new Message<>(200, message, null, Message.MessageType.RESPONSE);
    }



    public static <T> Message<T> ok(T body) {
        return new Message<>(200, "OK", body, Message.MessageType.RESPONSE);
    }


    public Message(int status, String message, T body, MessageType messageType) {
        this.status = status;
        this.message = message;
        this.body = body;
        this.messageType = messageType;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(int status, String message) {
        this(status, message, null, null);
    }

    public Message(int status) {
        this(status, null, null, null);
    }
    // --- Getters and setters ---

    public int getStatus() { return status; }

    public Message<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() { return message; }

    public Message<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getBody() { return body; }

    public Message<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public long getTimestamp() { return timestamp; }

    public MessageType getMessageType() { return messageType; }

    public Message<T> setMessageType(MessageType messageType) {
        this.messageType = messageType;
        return this;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}


