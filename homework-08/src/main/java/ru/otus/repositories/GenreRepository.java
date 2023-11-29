package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
