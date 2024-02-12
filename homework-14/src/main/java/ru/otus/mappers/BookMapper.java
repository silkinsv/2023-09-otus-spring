package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.models.documents.AuthorDocument;
import ru.otus.models.documents.BookDocument;
import ru.otus.models.documents.GenreDocument;
import ru.otus.models.entities.Book;

@Component
@RequiredArgsConstructor
public class BookMapper {
    public BookDocument toDocument(Book book) {
        return new BookDocument(book.getId().toString(),
                book.getTitle(),
                new AuthorDocument(book.getAuthor().getId().toString(), book.getAuthor().getFullName()),
                new GenreDocument(book.getGenre().getId().toString(), book.getGenre().getName()));
    }
}
