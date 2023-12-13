package ru.otus.services;

import ru.otus.dto.GenreDto;
import ru.otus.models.Genre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    List<GenreDto> findAll();

    Set<Genre> findByIds(List<Long> genresIds);
}
