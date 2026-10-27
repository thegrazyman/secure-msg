package net.thijmen.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SecureMsgClient implements ClientModInitializer {
    private static List<PlayerKeyEntry> playerKeyEntries = new ArrayList<>();

    static {
        try {
            playerKeyEntries.add(new PlayerKeyEntry("tmp", SecureMsgEncryptionHelper.generateKey()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            for (int i = 0; i < 5; i++) {
                List<String> list = new ArrayList<String>();
                list.add("Pieter");
                list.add("Jan");
                list.add("Bart");
                ChatRooms.getChatRooms().add(new ChatRoom("Testroom" + i, list, SecureMsgEncryptionHelper.generateKey()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PlayerKeyEntry> getPlayerKeyEntries() {
        return playerKeyEntries;
    }

    public static void setPlayerKeyEntries(List<PlayerKeyEntry> playerKeyEntries) {
        SecureMsgClient.playerKeyEntries = playerKeyEntries;
    }

    @Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
			dispatcher.register(ClientCommands.literal("smsg")
					.then(ClientCommands.argument("playerName", StringArgumentType.word())
					.then(ClientCommands.argument("message", StringArgumentType.greedyString())
					.executes(SecureMsgCommands::SMSG))));
		});

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            dispatcher.register(ClientCommands.literal("reload_config_SMSG")
                    .executes(SecureMsgCommands::reloadConfig));
        });

        ConfigurationHandler.readConfig();
	}

    public static void sendMessage(String message, String playerName) {
        Minecraft client = Minecraft.getInstance();
        List<PlayerKeyEntry> playerKeyEntries = SecureMsgClient.getPlayerKeyEntries();
        String encryptedMessage;
        String parsedMessage;
        SecretKey key = playerKeyEntries.stream().filter(n -> n.getPlayerName().equals(playerName)).findFirst().orElse(null).getKey();

        try {
            encryptedMessage = SecureMsgEncryptionHelper.encryptString(message, key);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        parsedMessage = "SMG.@&." + encryptedMessage;

        client.player.connection.sendCommand("msg " + playerName + " " + parsedMessage);
        client.player.sendSystemMessage(Component.literal("Sent " + playerName + ": " + message).withColor(TextColor.GRAY));
    }
}