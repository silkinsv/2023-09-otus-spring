package ru.otus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.dto.GenreDto;
import ru.otus.mappers.GenreMapper;
import ru.otus.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getGenres() {
        return genreRepository.findAll()
                .map(genreMapper::toDto);
    }
}
