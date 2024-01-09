package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.AuthorDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.AuthorMapper;
import ru.otus.models.Author;
import ru.otus.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public Author findById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(id)));
    }
}
