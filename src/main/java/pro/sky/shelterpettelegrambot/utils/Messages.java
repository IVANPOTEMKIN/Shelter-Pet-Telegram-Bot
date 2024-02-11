package pro.sky.shelterpettelegrambot.utils;

public class Messages {
    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("""
                Добро пожаловать, %s!
                Чем я могу Вам помочь?""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажми /start, чтобы вывелось стартовое сообщение
                Нажми /help, чтобы вывелось данное сообщение снова
                Нажми /settings, чтобы вывелась клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("""
                %s, выбери команды из списка ниже.""", userFirstname);
    }

    public static String DEFAULT_REACTION(String userFirstName) {
        return String.format("""
                Не могу понять чего Вы хотите, %s!
                Пожалуйста, выберите команду из списка.""", userFirstName);
    }
}