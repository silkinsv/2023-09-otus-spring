package ru.otus.services;

import reactor.core.publisher.Flux;
import ru.otus.dto.AuthorDto;

public interface AuthorService {
    Flux<AuthorDto> findAll();
}
