package pro.sky.shelterpettelegrambot.utils;

public class Messages {
    public static final String SUCCESSFUL_REGISTRATION = "Для дальнейшей регистрации нажмите или введите команду /registration";
    public static final String NO_SUCCESSFUL_REGISTRATION = "Вы можете зарегистрироваться в любой удобный для Вас момент, " +
            "вызвав команду /registration, а пока Вам доступен ограниченный функционал.";

    public static final String INFO_ABOUT_SHELTER = "Как бы получена информация о приюте.";
    public static final String INFO_ABOUT_PROCESS = "Как бы получена информация о процессе получения животного.";
    public static final String REPORT_ABOUT_PET = "Как бы получен отчет о домашнем животном.";
    public static final String CALL_VOLUNTEER = "Как бы вызван волонтер.";

    public static String REACTION_TO_FIRST_COMMAND_START(String userFirstName) {
        return String.format("""
                Добро пожаловать, %s!
                                
                Этот телеграм бот предназначен для людей, которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Прежде чем продолжить, рекомендую Вам зарегистрироваться, иначе Вам будет доступен не весь функционал.
                                
                Желаете продолжить и зарегистрироваться?""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("С возвращением, %s!", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("""
                %s, пожалуйста, выберите команду из клавиатуры ниже.""", userFirstname);
    }

    public static String DEFAULT_REACTION(String userFirstName) {
        return String.format("""
                %s, пожалуйста, выберите команду из списка меню.""", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_REGISTRATION(String userFirstName) {
        return String.format("Поздравляю, %s, Вы успешно зарегистрированы!", userFirstName);
    }
}