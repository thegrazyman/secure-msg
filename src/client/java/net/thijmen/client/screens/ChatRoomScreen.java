package net.thijmen.client.screens;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.thijmen.client.ChatRoom;

public class ChatRoomScreen extends Screen {
    ChatRoom chatRoom;
    EditBox keyEditBox;
    protected ChatRoomScreen(Component title, ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        super(title);
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.1), (int) (height * 0.05), Component.literal(chatRoom.getRoomName()), minecraft.font));
        this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.3), (int) (height * 0.05), Component.literal("Key: "), minecraft.font));
        keyEditBox = new EditBox(minecraft.font,(int) (width * 0.4),(int) (height * 0.03),(int) (width * 0.5), (int) (height * 0.08), Component.empty());
        this.addRenderableWidget(keyEditBox);
    }
}
