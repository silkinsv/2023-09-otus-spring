package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.models.documents.AuthorDocument;
import ru.otus.models.entities.Author;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
    public AuthorDocument toDocument(Author author) {
        return new AuthorDocument(author.getId().toString(), author.getFullName());
    }
}
