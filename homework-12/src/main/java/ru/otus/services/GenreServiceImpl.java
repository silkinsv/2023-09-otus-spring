package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.GenreDto;
import ru.otus.mappers.GenreMapper;
import ru.otus.models.Genre;
import ru.otus.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public List<Genre> findByIds(List<Long> ids) {
        return genreRepository.findAllById(ids);
    }
}
