package io.github.stardew.mini.Model;

import java.util.UUID;

public class AudioTrack {
    private String id;
    private String ownerUsername;
    private String name;
    private byte[] data;

    public AudioTrack() {
        this.id = UUID.randomUUID().toString();
    }

    public AudioTrack(String ownerUsername, String name, byte[] data) {
        this();
        this.ownerUsername = ownerUsername;
        this.name = name;
        this.data = data;
    }

    public String getId() { return id; }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}

