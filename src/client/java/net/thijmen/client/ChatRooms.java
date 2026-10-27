package net.thijmen.client;

import java.util.ArrayList;
import java.util.List;

public class ChatRooms {
    private static List<ChatRoom> chatRooms = new ArrayList<>();

    public static List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public static void setChatRooms(List<ChatRoom> chatRooms) {
        ChatRooms.chatRooms = chatRooms;
    }
}