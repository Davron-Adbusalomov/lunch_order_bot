package org.example.config;

import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramConfig extends TelegramLongPollingBot {

    @Autowired
    private ChatService chatService;

    public TelegramConfig(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId;
        String data;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            data = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            data = update.getCallbackQuery().getData();
        } else {
            return;
        }

        try {
            chatService.chat(chatId, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "t.me/LunchOrderBot";
    }

    @Override
    public String getBotToken(){
        return "7140529979:AAGM4U2knSCn8JBFq_kqWJh_oDTyvEhKFLw";
    }
}
