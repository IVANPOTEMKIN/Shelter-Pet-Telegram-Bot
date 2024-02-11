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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.shelterpettelegrambot.configuration.BotConfiguration;
import pro.sky.shelterpettelegrambot.model.User;
import pro.sky.shelterpettelegrambot.model.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String userFirstName = update.getMessage().getChat().getFirstName();
        String answer;

        if (update.hasMessage() && update.getMessage().hasText()) {

            switch (text) {
                case COMMAND_START:

                    if (userRepository.findById(chatId).isEmpty()) {
                        answer = REACTION_TO_FIRST_COMMAND_START(userFirstName);
                        reactionToCommand(chatId, answer);
                        saveNewUserToDB(chatId, userFirstName);
                        return;
                    }

                    answer = REACTION_TO_COMMAND_START(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;

                case COMMAND_HELP:
                    answer = REACTION_TO_COMMAND_HELP(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;

                case COMMAND_SETTINGS:
                    answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                    getKeyBoard(chatId, answer);
                    break;

                default:
                    answer = DEFAULT_REACTION(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;
            }
        }
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

    private void getKeyBoard(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add(COMMAND_START);
        row.add(COMMAND_HELP);
        row.add(COMMAND_SETTINGS);

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