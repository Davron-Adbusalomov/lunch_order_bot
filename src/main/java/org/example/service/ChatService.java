package org.example.service;

import org.example.config.TelegramConfig;
import org.example.enums.ChatSessionState;
import org.example.enums.ChatState;
import org.example.model.Employee;
import org.example.model.Meal;
import org.example.model.Order;
import org.example.model.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MealService mealService;

    @Autowired
    private OrderService orderService;

    TelegramConfig telegramConfig = new TelegramConfig(this);

    public void chat(Long chatId, String msg) throws Exception {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        ChatState step = ChatSessionState.STATE_MAP.getOrDefault(String.valueOf(chatId),ChatState.START);
        Integer id = ChatSessionState.ID_MAP.getOrDefault(chatId, null);


        switch (step) {
            case START:
                if (msg.equals("/start")) {
                    sendMessage.setText("Assalomu alaykum. LunchBotga xush kelibsiz! Iltimos identifikatsiya kodini kiriting:");
                    telegramConfig.execute(sendMessage);
                    ChatSessionState.setState(String.valueOf(chatId), ChatState.CODE_VALIDATION);
                }
                break;
            case CODE_VALIDATION:
                try {
                    int code = Integer.parseInt(msg);
                    //  Employee employee = restTemplate.getForObject("http://localhost:8080/employees/getEmployeeByCode/" + code, Employee.class);
                    Employee employee = employeeService.getEmployeeByCode(code);
                    sendMessage.setText("Xurmatli, " + employee.getLastName() + " " + employee.getFirstName() + ", bugungi taomnoma bilan tanishish uchun menuni oling!");

                    ChatSessionState.setId(chatId, employee.getId());

                    List<String> buttonLabels = new ArrayList<>();
                    buttonLabels.add("Menuni olish");
                    sendMessage.setReplyMarkup(createInlineKeyboardMarkup(buttonLabels));

                    telegramConfig.execute(sendMessage);
                } catch (Exception e) {
                    sendMessage.setText("Xatolik yuz berdi: " + e.getMessage());
                    telegramConfig.execute(sendMessage);
                }
                ChatSessionState.setState(String.valueOf(chatId), ChatState.ASK_MENU);
                break;
            case ASK_MENU:
                if(msg.equals("Menuni olish")){
                    try {
                        List<Meal> meals = mealService.getMealsByWeekDay(LocalDateTime.now().getDayOfWeek());
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
                        sendMessage.setReplyMarkup(createInlineKeyboardMarkup(buttonLabels));

                        telegramConfig.execute(sendMediaGroup);
                        telegramConfig.execute(sendMessage);
                    }catch (Exception e){
                        sendMessage.setText(e.getMessage());
                        telegramConfig.execute(sendMessage);
                    }
                }
                ChatSessionState.setState(String.valueOf(chatId), ChatState.BOOK_LUNCH);
                break;
            case BOOK_LUNCH:
                if (msg.equals("Ha")){
                    try {
                        Order order = new Order();
                        order.setEmployee_id(id);

                        List<Integer> meal_ids = new ArrayList<>();
                        for (Meal meal:mealService.getMealsByWeekDay(LocalDateTime.now().getDayOfWeek())) {
                            meal_ids.add(meal.getId());
                        }
                        order.setMeals_id(meal_ids);
                        orderService.assignMeals(order);
                        sendMessage.setText("Siz muvaffaqqiyatli buyurtma berdingiz! Quyida tugma orqali bugungi buyurtmalar statistikasini olishingiz mumkin!");
                        sendMessage.setReplyMarkup(createReplyKeyboardMarkup("Statistikani olish"));
                        telegramConfig.execute(sendMessage);
                    }catch (Exception e){
                        sendMessage.setText(e.getMessage());
                        telegramConfig.execute(sendMessage);
                    }
                }
                ChatSessionState.setState(String.valueOf(chatId), ChatState.ASK_STATISTICS);
                break;
            case ASK_STATISTICS:
                if (msg.equals("Statistikani olish")){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (OrderStatistics orderStatistics: orderService.getStatistics()){
                        stringBuilder.append(orderStatistics.getEmployeeId()).append(" ").append(orderStatistics.getMealId()).append("\n");
                    }
                    sendMessage.setText(stringBuilder.toString());
                }
                telegramConfig.execute(sendMessage);

        }
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup(List<String> buttonLabels) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (String label : buttonLabels) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(label);
            inlineKeyboardButton.setCallbackData(label);
            rowInline.add(inlineKeyboardButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(String buttonLabel) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(buttonLabel);
        keyboardRow.add(keyboardButton);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }
}
