package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private Author author;

    private Set<Genre> genres;

    public Book(String title, Author author, Set<Genre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
