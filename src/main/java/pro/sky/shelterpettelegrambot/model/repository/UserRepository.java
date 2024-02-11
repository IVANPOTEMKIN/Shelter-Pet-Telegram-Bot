package pro.sky.shelterpettelegrambot.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.shelterpettelegrambot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}