package net.thijmen.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.thijmen.client.PlayerKeyEntry;
import net.thijmen.client.SecureMsgClient;
import net.thijmen.client.SecureMsgEncryptionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Mixin(net.minecraft.client.multiplayer.chat.ChatListener.class)
public class ChatListenerMixin {

    @Inject(at = @At("HEAD"), method = "handlePlayerChatMessage", cancellable = true)
    public void inject(CallbackInfo ci, @Local PlayerChatMessage message) {
        Minecraft client = Minecraft.getInstance();
        List<PlayerKeyEntry> playerKeyEntries = SecureMsgClient.getPlayerKeyEntries();
        UUID uuid = message.sender();
        Player player = Minecraft.getInstance().player;
        Player sender = Minecraft.getInstance()
                .level.getPlayerByUUID(uuid);
        if (sender == null) {
            System.err.println("Message sender null");
            return;
        }
        if (message.signedContent().startsWith("SMG.@&.")) {
            if (message.sender().equals(client.player.getUUID())) {
                ci.cancel();
                return;
            }
            System.out.println("Detected signature in message: " + message.signedContent() + ". \nFrom: " + sender.getName().getString());
            ci.cancel();

            String[] strings = message.signedContent().split(".@&.");
            String encryptedString = strings[1];
            String decryptedString;
            try {
                decryptedString = SecureMsgEncryptionHelper.decryptString(encryptedString, playerKeyEntries.stream().filter(n -> n.getPlayerName().equals(sender.getName().getString())).findFirst().orElse(null).getKey());
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

            player.sendSystemMessage(Component.literal(sender.getName().getString() + ": " + decryptedString).withColor(TextColor.GRAY));
        }
    }
}
