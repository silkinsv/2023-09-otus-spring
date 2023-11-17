package ru.otus.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@ToString(exclude = {"book"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @ManyToOne(targetEntity = Book.class)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
