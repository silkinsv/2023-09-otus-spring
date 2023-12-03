package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
