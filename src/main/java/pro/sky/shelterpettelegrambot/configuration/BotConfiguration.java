package pro.sky.shelterpettelegrambot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("application.properties")
@Data
@Configuration
public class BotConfiguration {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;
}