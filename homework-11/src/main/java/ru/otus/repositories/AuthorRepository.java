package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.models.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {
}
