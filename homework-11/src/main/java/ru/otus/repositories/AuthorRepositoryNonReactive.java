package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Author;

public interface AuthorRepositoryNonReactive extends MongoRepository<Author, String> {
}
