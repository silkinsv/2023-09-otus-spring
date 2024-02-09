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
    public BookDocument toDocument(Book book, AuthorDocument authorDocument, GenreDocument genreDocument) {
        return new BookDocument(book.getTitle(), authorDocument, genreDocument);
    }
}
