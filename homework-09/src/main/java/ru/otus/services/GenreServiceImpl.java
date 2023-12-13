package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dto.GenreDto;
import ru.otus.models.Genre;
import ru.otus.repositories.GenreRepository;
import ru.otus.services.utils.DtoConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(DtoConverter::convertToGenreDto)
                .toList();
    }

    @Override
    public Set<Genre> findByIds(List<Long> ids) {
        return new HashSet<>(genreRepository.findAllById(ids));
    }
}
