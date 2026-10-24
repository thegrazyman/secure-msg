package net.thijmen.client;

import javax.crypto.SecretKey;

public class PlayerKeyEntry {
    public PlayerKeyEntry(String name, SecretKey key) {
        this.key = key;
        this.playerName = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    private String playerName;
    private SecretKey key;
}
