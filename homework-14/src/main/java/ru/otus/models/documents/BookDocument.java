package ru.otus.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "books")
public class BookDocument {
    @Id
    private String id;

    private String title;

    @DBRef
    private AuthorDocument author;

    @DBRef
    private GenreDocument genre;

    public BookDocument(String title, AuthorDocument author, GenreDocument genre) {
        this.title = title;
        this.genre = genre;
        this.author = author;
    }
}
