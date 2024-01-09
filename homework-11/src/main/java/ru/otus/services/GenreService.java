package ru.otus.services;

import ru.otus.dto.GenreDto;
import ru.otus.models.Genre;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();

    List<Genre> findByIds(List<String> genresIds);
}
