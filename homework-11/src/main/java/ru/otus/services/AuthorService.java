package ru.otus.services;

import ru.otus.dto.AuthorDto;
import ru.otus.models.Author;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    Author findById(String id);
}
