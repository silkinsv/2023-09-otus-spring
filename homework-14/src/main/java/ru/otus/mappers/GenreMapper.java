package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.models.documents.GenreDocument;
import ru.otus.models.entities.Genre;

@Component
@RequiredArgsConstructor
public class GenreMapper {
    public GenreDocument toDocument(Genre genre) {
        return new GenreDocument(genre.getId().toString(), genre.getName());
    }
}
