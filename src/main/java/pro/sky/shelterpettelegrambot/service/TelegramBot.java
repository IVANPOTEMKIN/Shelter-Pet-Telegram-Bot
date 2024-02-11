package pro.sky.shelterpettelegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.shelterpettelegrambot.configuration.BotConfiguration;

import java.util.ArrayList;
import java.util.List;

import static pro.sky.shelterpettelegrambot.utils.Commands.COMMAND_HELP;
import static pro.sky.shelterpettelegrambot.utils.Commands.COMMAND_START;
import static pro.sky.shelterpettelegrambot.utils.Descriptions.DESCRIPTION_COMMAND_HELP;
import static pro.sky.shelterpettelegrambot.utils.Descriptions.DESCRIPTION_COMMAND_START;
import static pro.sky.shelterpettelegrambot.utils.Messages.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfiguration configuration;

    public TelegramBot(BotConfiguration configuration) {
        super(configuration.getToken());
        this.configuration = configuration;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(COMMAND_START, DESCRIPTION_COMMAND_START));
        listOfCommands.add(new BotCommand(COMMAND_HELP, DESCRIPTION_COMMAND_HELP));

        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("ERROR: setting bot`s command list {}", e.getMessage());
        }
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
                    answer = REACTION_TO_COMMAND_START(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;
                case COMMAND_HELP:
                    answer = REACTION_TO_COMMAND_HELP(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;
                default:
                    answer = DEFAULT_REACTION(userFirstName);
                    reactionToCommand(chatId, answer);
                    break;
            }
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

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }
}