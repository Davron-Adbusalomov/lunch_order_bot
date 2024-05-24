package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = "org.example")
public class BotRegisterConfiguration {
    @Autowired
    private final TelegramConfig telegramConfig;

    public  BotRegisterConfiguration(TelegramConfig telegramConfig){
        this.telegramConfig=telegramConfig;
        System.out.println("BotRegisterConfiguration initialized with TelegramConfig: " + telegramConfig);
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        System.out.println("registerBot() called");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            System.out.println("Attempting to register bot");
            botsApi.registerBot(telegramConfig);
            System.out.println("Bot registered successfully");
        } catch (TelegramApiException e) {
            System.out.println("TelegramApiException occurred while registering bot");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception occurred while registering bot");
            e.printStackTrace();
        }
    }
}