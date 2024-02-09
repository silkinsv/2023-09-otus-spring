package ru.otus.models.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = Book.WITH_AUTHOR_GENRES_GRAPH, attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("genre")
        }),
        @NamedEntityGraph(name = Book.WITH_AUTHOR_GRAPH, attributeNodes = {
                @NamedAttributeNode("author")
        })
})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@Getter
public class Book {
    public static final String WITH_AUTHOR_GRAPH = "book-with-author-graph";

    public static final String WITH_AUTHOR_GENRES_GRAPH = "book-with-author-genres-graph";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
