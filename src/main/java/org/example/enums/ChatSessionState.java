package org.example.enums;

import org.example.model.Employee;
import org.example.service.ChatService;

import java.util.HashMap;


public class ChatSessionState {
    public static final HashMap<String, ChatState> STATE_MAP = new HashMap<>();

    public static ChatState getState(String chatId) {
        return STATE_MAP.get(chatId);
    }

    public static void setState(String chatId, ChatState state) {
        STATE_MAP.put(chatId, state);
    }

    public static final HashMap<Long, Integer> ID_MAP = new HashMap<>();

    public static Integer getEmployeeId(Long chatId){
        return ID_MAP.get(chatId);
    }

    public static void setId(Long chatId, Integer id) {
        ID_MAP.put(chatId, id);
    }
}
