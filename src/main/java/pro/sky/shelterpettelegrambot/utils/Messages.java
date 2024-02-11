package pro.sky.shelterpettelegrambot.utils;

public class Messages {
    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("Добро пожаловать, %S!\n" +
                "Чем я могу Вам помочь?", userFirstName);
    }
}