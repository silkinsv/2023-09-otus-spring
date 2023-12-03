package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String text;

    private String login;

    private Instant timestamp;

    @DBRef
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.login = System.getProperty("user.name");
        this.timestamp = Instant.now();
        this.book = book;
    }
}
