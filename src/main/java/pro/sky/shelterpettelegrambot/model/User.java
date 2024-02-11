package pro.sky.shelterpettelegrambot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "register_at")
    private Timestamp registerAt;
}