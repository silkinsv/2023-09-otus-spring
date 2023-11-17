package ru.otus.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;


@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = Book.WITH_AUTHOR_GRAPH, attributeNodes = {
                @NamedAttributeNode("author")
        })
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    public static final String WITH_AUTHOR_GRAPH = "book-with-author-graph";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private Author author;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
}
