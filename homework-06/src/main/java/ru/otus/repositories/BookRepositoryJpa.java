package ru.otus.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> graph = em.getEntityGraph(Book.WITH_AUTHOR_GRAPH);
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), graph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(book -> em.remove(book));
    }
}
