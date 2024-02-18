package ru.otus.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.models.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = Book.WITH_AUTHOR_GENRES_GRAPH)
    Optional<Book> findById(long id);

    @EntityGraph(value = Book.WITH_AUTHOR_GRAPH)
    List<Book> findAll();
}
