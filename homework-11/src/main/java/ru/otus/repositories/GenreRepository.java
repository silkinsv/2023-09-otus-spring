package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.models.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, String> {
}
