package ru.otus.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long bookCount;
        String message;
        try {
            bookCount = bookRepository.count();
        } catch (RuntimeException ex) {
            message = ex.getMessage();
            return Health.down().status(Status.DOWN).withDetail("message", message)
                    .build();
        }

        if (bookCount == 0) {
            message = "В библиотеке отсутствуют книги";
            return Health.down().status(Status.DOWN).withDetail("message", message)
                    .build();
        } else {
            message = "Все в порядке. Книги есть";
            return Health.up().withDetail("message", message)
                    .build();
        }
    }
}
