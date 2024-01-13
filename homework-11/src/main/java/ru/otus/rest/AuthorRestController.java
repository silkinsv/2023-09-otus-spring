package ru.otus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.dto.AuthorDto;
import ru.otus.mappers.AuthorMapper;
import ru.otus.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getAuthors() {
        return authorRepository.findAll()
                .map(authorMapper::toDto);
    }
}
