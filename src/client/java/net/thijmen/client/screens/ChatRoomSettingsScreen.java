package net.thijmen.client.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.thijmen.client.ChatRoom;
import net.thijmen.client.ChatRooms;

import java.util.List;

public class ChatRoomSettingsScreen extends Screen {
    public ChatRoomSettingsScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new MultiLineTextWidget((int) (width * 0.1), (int) (height * 0.1), Component.literal("ChatRoomScreen"), minecraft.font));

        // for voor alle chatrooms
        List<ChatRoom> chatRooms = ChatRooms.getChatRooms();
        int size = chatRooms.size();
        int chatRoomsStartHeight = (int) (height * 0.2);
        for (int i = 0; i < size; i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            Button chatRoomButton = Button.builder(Component.literal(chatRoom.getRoomName()), (btn) -> {
                minecraft.gui.setScreen(new ChatRoomScreen(Component.empty(), chatRoom));
            }).bounds((int) (width * 0.1), chatRoomsStartHeight + (int) (height * 0.1 * i), (int) (width * 0.2), (int) (height * 0.08)).build();

            this.addRenderableWidget(chatRoomButton);
        }
    }
}
