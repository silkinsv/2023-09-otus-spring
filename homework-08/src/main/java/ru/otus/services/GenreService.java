package ru.otus.services;

import ru.otus.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    List<Genre> findAll();

    Set<Genre> findByIds(List<String> genresIds);
}
