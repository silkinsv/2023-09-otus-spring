package ru.otus.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
class BookRepositoryJpaTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final long FIRST_AUTHOR_ID = 1L;

    private static final long FIRST_GENRE_ID = 1L;

    private static final long SECOND_GENRE_ID = 2L;

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Book expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        var actualBook = repositoryJpa.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        TypedQuery<Book> query = em.getEntityManager().createQuery("select b from Book b", Book.class);

        var expectedBooks = query.getResultList();
        var actualBooks = repositoryJpa.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500", em.find(Author.class, FIRST_AUTHOR_ID),
                Set.of(em.find(Genre.class, FIRST_GENRE_ID), em.find(Genre.class, SECOND_GENRE_ID)));
        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(FIRST_BOOK_ID, "BookTitle_10500", em.find(Author.class, FIRST_AUTHOR_ID),
                Set.of(em.find(Genre.class, FIRST_GENRE_ID), em.find(Genre.class, SECOND_GENRE_ID)));

        assertThat(repositoryJpa.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repositoryJpa.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(repositoryJpa.findById(FIRST_BOOK_ID)).isPresent();
        repositoryJpa.deleteById(FIRST_BOOK_ID);
        assertThat(repositoryJpa.findById(FIRST_BOOK_ID)).isEmpty();
    }
}