package ru.otus.services;

import reactor.core.publisher.Flux;
import ru.otus.dto.GenreDto;

public interface GenreService {
    Flux<GenreDto> findAll();
}
