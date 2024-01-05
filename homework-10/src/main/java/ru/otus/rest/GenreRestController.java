package ru.otus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.dto.GenreDto;
import ru.otus.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getGenres() {
        return genreService.findAll();
    }
}
