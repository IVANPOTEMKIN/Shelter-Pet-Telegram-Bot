package pro.sky.shelterpettelegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.shelterpettelegrambot.configuration.BotConfiguration;
import pro.sky.shelterpettelegrambot.model.User;
import pro.sky.shelterpettelegrambot.model.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static pro.sky.shelterpettelegrambot.utils.Buttons.*;
import static pro.sky.shelterpettelegrambot.utils.Commands.*;
import static pro.sky.shelterpettelegrambot.utils.Descriptions.*;
import static pro.sky.shelterpettelegrambot.utils.Messages.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    private final BotConfiguration configuration;

    public TelegramBot(BotConfiguration configuration) {
        super(configuration.getToken());
        this.configuration = configuration;
        createMainMenu();
    }

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId;

        if (update.hasMessage() && update.getMessage().hasText()) {

            chatId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getChat().getFirstName();
            String text = update.getMessage().getText();

            if (userRepository.findById(chatId).isEmpty()) {
                getCommandsForUnregisteredUsers(chatId, text, userFirstName);
            } else {
                getCommandsForRegisteredUsers(chatId, text, userFirstName);
            }

        } else if (update.hasCallbackQuery()) {

            chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            getResultForKeyBoardButtons(chatId, callbackData);
        }
    }

    public void getCommandsForUnregisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            case COMMAND_START:
                answer = REACTION_TO_FIRST_COMMAND_START(userFirstName);
                buttonsForRegistration(chatId, answer);
                break;

            case COMMAND_HELP:
                answer = REACTION_TO_COMMAND_HELP(userFirstName);
                reactionToCommand(chatId, answer);
                break;

            case COMMAND_SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                getKeyBoardForUnregisteredUsers(chatId, answer);
                break;

            case COMMAND_REGISTRATION:
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                saveNewUserToDB(chatId, userFirstName);
                buttonGetInfoAboutShelter(chatId, answer);
                break;

            default:
                answer = DEFAULT_REACTION(userFirstName);
                reactionToCommand(chatId, answer);
                break;
        }
    }

    public void getCommandsForRegisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            case COMMAND_START:
                answer = REACTION_TO_COMMAND_START(userFirstName);
                buttonGetInfoAboutShelter(chatId, answer);
                break;

            case COMMAND_HELP:
                answer = REACTION_TO_COMMAND_HELP(userFirstName);
                buttonCallVolunteer(chatId, answer);
                break;

            case COMMAND_SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                getKeyBoardForRegisteredUsers(chatId, answer);
                break;

            case COMMAND_GET_INFO_ABOUT_SHELTER:
                answer = INFO_ABOUT_SHELTER;
                buttonGetInfoAboutProcess(chatId, answer);
                break;

            case COMMAND_GET_INFO_ABOUT_PROCESS:
                answer = INFO_ABOUT_PROCESS;
                buttonGetReportAboutPet(chatId, answer);
                break;

            case COMMAND_GET_REPORT_ABOUT_PET:
                answer = REPORT_ABOUT_PET;
                buttonCallVolunteer(chatId, answer);
                break;

            case COMMAND_CALL_VOLUNTEER:
                answer = CALL_VOLUNTEER;
                reactionToCommand(chatId, answer);
                break;

            default:
                answer = DEFAULT_REACTION(userFirstName);
                buttonCallVolunteer(chatId, answer);
                break;
        }
    }

    public void getResultForKeyBoardButtons(Long chatId, String callbackData) {
        String answer;

        switch (callbackData) {

            case YES_BUTTON:
                answer = SUCCESSFUL_REGISTRATION;
                reactionToCommand(chatId, answer);
                break;

            case NO_BUTTON:
                answer = NO_SUCCESSFUL_REGISTRATION;
                reactionToCommand(chatId, answer);
                break;

            case INFO_ABOUT_SHELTER_BUTTON:
                answer = INFO_ABOUT_SHELTER;
                buttonGetInfoAboutProcess(chatId, answer);
                break;

            case INFO_ABOUT_PROCESS_BUTTON:
                answer = INFO_ABOUT_PROCESS;
                buttonGetReportAboutPet(chatId, answer);
                break;

            case REPORT_ABOUT_PET_BUTTON:
                answer = REPORT_ABOUT_PET;
                buttonCallVolunteer(chatId, answer);
                break;

            case CALL_VOLUNTEER_BUTTON:
                answer = CALL_VOLUNTEER;
                reactionToCommand(chatId, answer);
                break;

            case DETAILED_INFORMATION_BUTTON_1:
                answer = DETAILED_INFORMATION;
                getKeyBoardGetInfoAboutShelter(chatId, answer);
                break;

            case DETAILED_INFORMATION_BUTTON_2:
                answer = DETAILED_INFORMATION;
                getKeyBoardGetInfoAboutProcess(chatId, answer);
                break;

            case DETAILED_INFORMATION_BUTTON_3:
                answer = DETAILED_INFORMATION;
                getKeyBoardGetInfoAboutShelter(chatId, answer);
                break;
        }
    }

    private void getKeyBoardForUnregisteredUsers(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(COMMAND_START);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_HELP);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_SETTINGS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_REGISTRATION);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void getKeyBoardForRegisteredUsers(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_REPORT_ABOUT_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void getKeyBoardGetInfoAboutShelter(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_SHELTER_WORK_SCHEDULE_AND_ADDRESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_SECURITY_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_GENERAL_SAFETY_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void getKeyBoardGetInfoAboutProcess(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_LIST_OF_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_RULES_OF_DATING);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_LIST_OF_DOCS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_TRANSPORTATION_RECOMMENDATIONS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_YOUNG_PET_HOUSE_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_OLD_PET_HOUSE_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_INVALIDE_PET_HOUSE_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_TIPS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_RECOMMENDATION_OF_DOG_HANDLER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_GET_INFO_ABOUT_PROCESS_REASONS_FOR_REFUSAL);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(COMMAND_CALL_VOLUNTEER);
        rows.add(row);
        
        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonsForRegistration(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonYes = new InlineKeyboardButton();
        var buttonNo = new InlineKeyboardButton();

        buttonYes.setText("Да");
        buttonYes.setCallbackData(YES_BUTTON);

        buttonNo.setText("Нет");
        buttonNo.setCallbackData(NO_BUTTON);

        row.add(buttonYes);
        row.add(buttonNo);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonGetInfoAboutShelter(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonGetInfoAboutShelter = new InlineKeyboardButton();

        buttonGetInfoAboutShelter.setText(COMMAND_GET_INFO_ABOUT_SHELTER);
        buttonGetInfoAboutShelter.setCallbackData(INFO_ABOUT_SHELTER_BUTTON);

        row.add(buttonGetInfoAboutShelter);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonGetInfoAboutProcess(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonGetInfoAboutProcess = new InlineKeyboardButton();
        var buttonGetDetailedInfo = new InlineKeyboardButton();

        buttonGetInfoAboutProcess.setText(COMMAND_GET_INFO_ABOUT_PROCESS);
        buttonGetInfoAboutProcess.setCallbackData(INFO_ABOUT_PROCESS_BUTTON);

        buttonGetDetailedInfo.setText(COMMAND_DETAILED_INFORMATION);
        buttonGetDetailedInfo.setCallbackData(DETAILED_INFORMATION_BUTTON_1);

        row.add(buttonGetInfoAboutProcess);
        row.add(buttonGetDetailedInfo);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonGetReportAboutPet(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonGetReportAboutPet = new InlineKeyboardButton();
        var buttonGetDetailedInfo = new InlineKeyboardButton();

        buttonGetReportAboutPet.setText(COMMAND_GET_REPORT_ABOUT_PET);
        buttonGetReportAboutPet.setCallbackData(REPORT_ABOUT_PET_BUTTON);

        buttonGetDetailedInfo.setText(COMMAND_DETAILED_INFORMATION);
        buttonGetDetailedInfo.setCallbackData(DETAILED_INFORMATION_BUTTON_2);

        row.add(buttonGetReportAboutPet);
        row.add(buttonGetDetailedInfo);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void buttonCallVolunteer(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var buttonCallVolunteer = new InlineKeyboardButton();
        var buttonGetDetailedInfo = new InlineKeyboardButton();

        buttonCallVolunteer.setText(COMMAND_CALL_VOLUNTEER);
        buttonCallVolunteer.setCallbackData(CALL_VOLUNTEER_BUTTON);

        buttonGetDetailedInfo.setText(COMMAND_DETAILED_INFORMATION);
        buttonGetDetailedInfo.setCallbackData(DETAILED_INFORMATION_BUTTON_3);

        row.add(buttonCallVolunteer);
        row.add(buttonGetDetailedInfo);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

    private void saveNewUserToDB(Long chatId, String userFirstName) {
        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(userFirstName);
        user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }

    private void createMainMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand(COMMAND_START, DESCRIPTION_COMMAND_START));
        listOfCommands.add(new BotCommand(COMMAND_HELP, DESCRIPTION_COMMAND_HELP));
        listOfCommands.add(new BotCommand(COMMAND_SETTINGS, DESCRIPTION_COMMAND_SETTINGS));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("ERROR: setting bot`s command list {}", e.getMessage());
        }
    }

    private void reactionToCommand(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);
        executeMessage(message);
    }

    private SendMessage sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }
}