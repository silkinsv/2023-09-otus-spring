package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Book;

public interface BookRepositoryNonReactive extends MongoRepository<Book, String> {
}
