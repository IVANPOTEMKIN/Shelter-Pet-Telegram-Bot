package pro.sky.shelterpettelegrambot.utils;

public class Messages {
    public static final String SUCCESSFUL_REGISTRATION = "Для дальнейшей регистрации нажмите или введите команду /registration";
    public static final String NO_SUCCESSFUL_REGISTRATION = "Вы можете зарегистрироваться в любой удобный для Вас момент, " +
            "вызвав команду /registration, а пока Вам доступен ограниченный функционал.";

    public static String REACTION_TO_FIRST_COMMAND_START(String userFirstName) {
        return String.format("""
                Добро пожаловать, %s!
                
                Этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                
                Напишите, пожалуйста, чем я могу Вам помочь, но прежде зарегистрируйтесь, иначе Вам не будет доступен весь функционал.
                
                Желаете продолжить и зарегистрироваться?""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("""
                С возвращением, %s!
                
                Напишите, пожалуйста, что я могу для Вас сделать или выберите команду из списка меню.""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажми /start, чтобы вывелось стартовое сообщение
                
                Нажми /help, чтобы вывелось данное сообщение снова
                
                Нажми /registration, чтобы зарегистрироваться
                
                Нажми /settings, чтобы вывелась клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("""
                %s, выберите команду из клавиатуры ниже.""", userFirstname);
    }

    public static String DEFAULT_REACTION(String userFirstName) {
        return String.format("""
                %s, пожалуйста, выберите команду из списка меню.""", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_REGISTRATION(String userFirstName) {
        return String.format("Поздравляю, %s, Вы успешно зарегистрированы!", userFirstName);
    }
}