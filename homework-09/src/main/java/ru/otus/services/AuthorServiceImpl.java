package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.AuthorDto;
import ru.otus.models.Author;
import ru.otus.repositories.AuthorRepository;
import ru.otus.services.utils.DtoConverter;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(DtoConverter::convertToAuthorDto)
                .toList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }
}
