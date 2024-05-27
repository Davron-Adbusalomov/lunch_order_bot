package org.example.service;

import org.example.config.TelegramConfig;
import org.example.controller.EmployeeController;
import org.example.controller.MealController;
import org.example.controller.OrderController;
import org.example.enums.ChatSessionState;
import org.example.enums.ChatState;
import org.example.model.Employee;
import org.example.model.Meal;
import org.example.model.Order;
import org.example.model.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private EmployeeController employeeController;

    @Autowired
    private MealController mealController;

    @Autowired
    private OrderController orderController;

    TelegramConfig telegramConfig = new TelegramConfig(this);

    public void chat(Long chatId, String msg) throws Exception {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (msg.equals("/start")) {
            ChatSessionState.setState(chatId, ChatState.START);
        }

        ChatState step = ChatSessionState.STATE_MAP.getOrDefault(chatId,ChatState.START);
        Integer id = ChatSessionState.ID_MAP.getOrDefault(chatId, null);


        switch (step) {
            case START:
                if (msg.equals("/start")) {
                    sendMessage.setText("Assalomu alaykum. LunchBotga xush kelibsiz!\nIltimos identifikatsiya kodini kiriting:");
                    telegramConfig.execute(sendMessage);
                    ChatSessionState.setState(chatId, ChatState.CODE_VALIDATION);
                }
                break;
            case CODE_VALIDATION:
                try {
                    if (msg.matches("\\d+")) {
                        int code = Integer.parseInt(msg);
                        Employee employee = employeeController.getEmployeeByCode(code);
                        sendMessage.setText("Xurmatli, " + employee.getLastName() + " " + employee.getFirstName() + ", bugungi taomnoma bilan tanishish uchun menuni oling❕");

                        ChatSessionState.setId(chatId, employee.getId());

                        List<String> buttonLabels = new ArrayList<>();
                        buttonLabels.add("Menuni olish");
                        sendMessage.setReplyMarkup(ButtonsService.createInlineKeyboardMarkup(buttonLabels));

                        telegramConfig.execute(sendMessage);
                    }else {
                        sendMessage.setText("Code faqat raqamlardan iborat bo'lishi kerak❗ Iltimos o'z kodingizni kiriting!");
                        telegramConfig.execute(sendMessage);
                        return;
                    }
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi: " + e.getMessage());
                    telegramConfig.execute(sendMessage);
                    return;
                }
                ChatSessionState.setState(chatId, ChatState.ASK_MENU);
                break;
            case ASK_MENU:
                if(msg.equals("Menuni olish")){
                    try {
                        List<Meal> meals = mealController.getMealByWeekDay(LocalDateTime.now().getDayOfWeek());
                        SendMediaGroup sendMediaGroup = new SendMediaGroup();
                        List<InputMedia> mediaList = new ArrayList<>();

                        InputMedia inputMedia1 = new InputMediaPhoto();
                        inputMedia1.setMedia(meals.get(0).getImages());
                        String allMealNames = meals.stream()
                                .map(Meal::getName)
                                .collect(Collectors.joining(", "));
                        inputMedia1.setCaption(allMealNames);
                        mediaList.add(inputMedia1);

                        for (int i =1; i<meals.size(); i++) {
                            String path = meals.get(i).getImages();
                            InputMedia inputMedia = new InputMediaPhoto();
                            inputMedia.setMedia(path);
                            mediaList.add(inputMedia);
                        }
                        sendMediaGroup.setChatId(chatId);
                        sendMediaGroup.setMedias(mediaList);

                        sendMessage.setText("Taomnoma bilan tanishdingiz! Hozir iste'mol qilishni xohlaysizmi?");

                        List<String> buttonLabels = new ArrayList<>();
                        buttonLabels.add("Ha");
                        buttonLabels.add("Yo'q");
                        sendMessage.setReplyMarkup(ButtonsService.createInlineKeyboardMarkup(buttonLabels));

                        telegramConfig.execute(sendMediaGroup);
                        telegramConfig.execute(sendMessage);
                    }catch (Exception e){
                        sendMessage.setText(e.getMessage());
                        telegramConfig.execute(sendMessage);
                        return;
                    }
                }
                ChatSessionState.setState(chatId, ChatState.BOOK_LUNCH);
                break;
            case BOOK_LUNCH:
                if (msg.equals("Ha")){
                    try {
                        Order order = new Order();
                        order.setEmployee_id(id);

                        List<Integer> meal_ids = new ArrayList<>();
                        for (Meal meal:mealController.getMealByWeekDay(LocalDateTime.now().getDayOfWeek())){
                            meal_ids.add(meal.getId());
                        }
                        order.setMeals_id(meal_ids);
                        orderController.recordLunchOrder(order);
                        sendMessage.setText("Siz muvaffaqqiyatli buyurtma berdingiz!✅\nQuyida tugma orqali bugungi buyurtmalar statistikasini olishingiz mumkin!");
                        sendMessage.setReplyMarkup(ButtonsService.createReplyKeyboardMarkup("Statistikani olish"));
                        telegramConfig.execute(sendMessage);
                    }catch (Exception e){
                        sendMessage.setText(e.getMessage());
                        telegramConfig.execute(sendMessage);
                    }
                } else if (msg.equals("Yo'q")) {
                    sendMessage.setText("Xo'p, buyurtmani keyinroq berishingiz mumkin!👌👌👌");
                    telegramConfig.execute(sendMessage);
                    return;
                } else {
                    sendMessage.setText("Iltimos tugmalardan birini tanlang!");
                    telegramConfig.execute(sendMessage);
                    return;
                }
                ChatSessionState.setState(chatId, ChatState.ASK_STATISTICS);
                break;
            case ASK_STATISTICS:
                if (msg.equals("Statistikani olish")){
                    try {
                        List<OrderStatistics> orderStatistics = orderController.getStatistics();
                        if (orderStatistics.isEmpty()){
                            sendMessage.setText("Hozircha hech qanday buyurtma qilinmagan❗❌");
                            telegramConfig.execute(sendMessage);
                            return;
                        }

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(String.format("%-30s %-20s%n", "Employee", "Meal"));
                        stringBuilder.append("------------------------------------------------------------\n");
                        for (OrderStatistics orderStatistic: orderStatistics){
                            stringBuilder.append(String.format("%-30s %-20s%n", orderStatistic.getEmployeeId()+"("+orderStatistic.getEmployeeName()+")", orderStatistic.getMealId()+"("+orderStatistic.getMealName()+")"));
                        }
                        sendMessage.setText(stringBuilder.toString());
                    }catch (Exception e){
                        sendMessage.setText(e.getMessage());
                    }
                }
                else {
                    sendMessage.setText("Xato input kiritdingiz❗❌");
                    telegramConfig.execute(sendMessage);
                    return;
                }
                telegramConfig.execute(sendMessage);
        }
    }
}
