package pro.sky.shelterpettelegrambot.utils;

public class Messages {
    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("Добро пожаловать, %s!\n" +
                "Чем я могу Вам помочь?", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP(String userFirstName) {
        return String.format("%s, этот телеграм бот предназначен для людей," +
                "которые заинтересованы в том, чтобы забрать из приюта домашнее животное.", userFirstName);
    }

    public static String DEFAULT_REACTION(String userFirstName) {
        return String.format("Не могу понять чего Вы хотите, %s!\n" +
                "Пожалуйста, выберите команду из списка.", userFirstName);
    }
}