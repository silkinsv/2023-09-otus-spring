package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
