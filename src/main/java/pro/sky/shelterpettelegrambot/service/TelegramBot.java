package pro.sky.shelterpettelegrambot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.sky.shelterpettelegrambot.configuration.BotConfiguration;

import static pro.sky.shelterpettelegrambot.utils.Commands.COMMAND_START;
import static pro.sky.shelterpettelegrambot.utils.Messages.REACTION_TO_COMMAND_START;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfiguration configuration;

    public TelegramBot(BotConfiguration configuration) {
        super(configuration.getToken());
        this.configuration = configuration;
    }

    @Override
    public void onUpdateReceived(Update update) {

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String userFirstName = update.getMessage().getChat().getFirstName();

        if (update.hasMessage() && update.getMessage().hasText()) {

            switch (text) {
                case COMMAND_START:
                    reactionToCommandStart(chatId, userFirstName);
                    break;
            }
        }
    }

    private void reactionToCommandStart(Long chatId, String userFirstName) {
        String text = REACTION_TO_COMMAND_START(userFirstName);
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