package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dto.AuthorDto;
import ru.otus.models.Author;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
