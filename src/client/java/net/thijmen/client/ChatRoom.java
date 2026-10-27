package net.thijmen.client;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String roomName;
    private List<String> playerNames = new ArrayList<>();
    private SecretKey key;

    public ChatRoom(String roomName, List<String> playerNames, SecretKey key) {
        this.roomName = roomName;
        this.playerNames = playerNames;
        this.key = key;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }
}
