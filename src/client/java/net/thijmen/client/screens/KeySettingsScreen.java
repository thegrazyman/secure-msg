package net.thijmen.client.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.thijmen.SecureMsg;
import net.thijmen.client.ConfigurationHandler;
import net.thijmen.client.PlayerKeyEntry;
import net.thijmen.client.SecureMsgClient;
import net.thijmen.client.SecureMsgEncryptionHelper;

import javax.crypto.SecretKey;
import java.util.HexFormat;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class KeySettingsScreen extends Screen {
    public EditBox keyEditBox;
    public EditBox playerNameEditBox;

    public KeySettingsScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        playerNameEditBox = new EditBox(minecraft.font, (int) (width * 0.10), (int) (height * 0.20), (int) (width * 0.3), (int) (height * 0.08), Component.empty());
        keyEditBox = new EditBox(minecraft.font, (int) (width * 0.45), (int) (height * 0.20), (int) (width * 0.3), (int) (height * 0.08), Component.empty());
        playerNameEditBox.setMaxLength(16);
        keyEditBox.setMaxLength(64);
        this.addRenderableWidget(playerNameEditBox);
        this.addRenderableWidget(keyEditBox);

        Button addEntryButton = Button.builder(Component.literal("Add"), (btn) -> {
            if (playerNameEditBox.getValue().contentEquals("")) return;
            if (keyEditBox.getValue().contentEquals("")) {
                try {
                    SecureMsgClient.getPlayerKeyEntries().add(new PlayerKeyEntry(playerNameEditBox.getValue(), SecureMsgEncryptionHelper.generateKey()));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }else {
                SecureMsgClient.getPlayerKeyEntries().add(new PlayerKeyEntry(playerNameEditBox.getValue(), SecureMsgEncryptionHelper.getKeyFromString(keyEditBox.getValue())));
            }
            reOpenScreen();
        }).bounds((int) (width * 0.80), (int) (height * 0.20), (int) (width * 0.08), (int) (height * 0.08)).build();
        this.addRenderableWidget(addEntryButton);

        Button randomKeyButton = Button.builder(Component.literal("Generate key"), (btn) -> {
            try {
                SecretKey key = SecureMsgEncryptionHelper.generateKey();
                String keyValue = SecureMsgEncryptionHelper.getString(key);
                minecraft.player.sendSystemMessage(Component.literal(keyValue));
                System.out.println("Key value: " + keyValue);
                keyEditBox.setValue(keyValue);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }).bounds((int) (width * 0.1), (int) (height * 0.9), (int) (width * 0.2), (int) (height * 0.08)).build();

        Button saveButton = Button.builder(Component.literal("Save config"), (btn) -> {
            ConfigurationHandler.writeConfig();
        }).bounds((int) (width * 0.3), (int) (height * 0.9), (int) (width * 0.2), (int) (height * 0.08)).build();

        Button reloadButton = Button.builder(Component.literal("reload config"), (btn) -> {
            ConfigurationHandler.readConfig();
            reOpenScreen();
        }).bounds((int) (width * 0.5), (int) (height * 0.9), (int) (width * 0.2), (int) (height * 0.08)).build();

        this.addRenderableWidget(reloadButton);
        this.addRenderableWidget(randomKeyButton);
        this.addRenderableWidget(saveButton);

        this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.10), (int) (height * 0.10), Component.literal("Player name"), minecraft.font));
        this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.45), (int) (height * 0.10), Component.literal("Key"), minecraft.font));


        // create the entry list
        int entriesStartHeight = (int) (height * 0.40);
        List<PlayerKeyEntry> playerKeyEntries = SecureMsgClient.getPlayerKeyEntries();

        for (int i = 0; i < playerKeyEntries.size(); i++) {
            PlayerKeyEntry entry = playerKeyEntries.get(i);
            this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.05), entriesStartHeight + (int) (height * 0.05 * i), Component.literal(playerKeyEntries.get(i).getPlayerName()), minecraft.font));
            EditBox editBox = new EditBox(minecraft.font, (int) (width * 0.35), entriesStartHeight + (int) (height * 0.05 * i), (int) (width * 0.60), (int) (height * 0.05), Component.empty());
            editBox.setMaxLength(64);
            editBox.setValue(SecureMsgEncryptionHelper.getString(playerKeyEntries.get(i).getKey()));
            this.addRenderableWidget(editBox);

            Button delButton = Button.builder(Component.literal("X"), (btn) -> {
                playerKeyEntries.remove(entry);
                reOpenScreen();
            }).bounds((int) (width * 0.95), entriesStartHeight + (int) (height * 0.05 * i), (int) (width * 0.03), (int) (width * 0.03)).build();

            Button saveEntryButton = Button.builder(Component.literal("S"), (btn) -> {
                entry.setKey(SecureMsgEncryptionHelper.getKeyFromString(editBox.getValue()));
                reOpenScreen();
            }).bounds((int) (width * 0.32), entriesStartHeight + (int) (height * 0.05 * i), (int) (width * 0.03), (int) (width * 0.03)).build();

            this.addRenderableWidget(delButton);
            this.addRenderableWidget(saveEntryButton);
        }
    }

    private void reOpenScreen() {
        minecraft.gui.setScreen(new KeySettingsScreen(Component.empty()));
    }
}
