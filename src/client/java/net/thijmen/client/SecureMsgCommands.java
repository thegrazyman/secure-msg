package net.thijmen.client;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecureMsgCommands {
    public static int SMSG(CommandContext<FabricClientCommandSource> context) {
        Player player = context.getSource().getPlayer();
        Minecraft client = context.getSource().getClient();

        String playerName = context.getArgument("playerName", String.class);
        String message = context.getArgument("message", String.class);

        //player.sendSystemMessage(Component.literal("Player: " + playerName));
        //player.sendSystemMessage(Component.literal("Message: "+message));

        SecureMsgClient.sendMessage(message, playerName);

        return 1;
    }

    public static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        ConfigurationHandler.readConfig();
        return 1;
    }
}
