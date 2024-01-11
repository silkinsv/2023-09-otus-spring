package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.models.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {
}
