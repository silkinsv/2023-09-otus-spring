package ru.otus.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.events.CommentsCascadeDeleteByBookEventsListener;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе mongodb для работы с книгами ")
@Import({CommentsCascadeDeleteByBookEventsListener.class})
@DataMongoTest
class BookRepositoryMongoTest {
    private static final String LOAD_BOOK_TITLE = "BookTitle_1";

    private static final String DELETE_BOOK_TITLE = "BookTitle_2";

    private static final String UPDATE_BOOK_TITLE = "BookTitle_3";

    private static final String FIRST_AUTHOR_FULLNAME = "Author_1";

    private static final String FIRST_GENRE_NAME = "Genre_1";

    private static final int COUNT_COMMENTS_BOOK_1 = 1;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(LOAD_BOOK_TITLE));
        var expectedBook = mongoTemplate.findOne(query, Book.class);
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var expectedBooks = mongoTemplate.findAll(Book.class);
        var actualBooks = bookRepository.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Query queryAuthor = new Query();
        queryAuthor.addCriteria(Criteria.where("fullName").is(FIRST_AUTHOR_FULLNAME));
        var author = mongoTemplate.findOne(queryAuthor, Author.class);

        Query queryGenre = new Query();
        queryGenre.addCriteria(Criteria.where("name").is(FIRST_GENRE_NAME));
        var genre = mongoTemplate.findOne(queryGenre, Genre.class);

        Query queryBook = new Query();
        queryGenre.addCriteria(Criteria.where("title").is("BookTitle_10500"));
        var nonExistingBookOptional = mongoTemplate.findOne(queryGenre, Book.class);

        Assertions.assertNull(nonExistingBookOptional);

        var expectedBook = new Book(null, "BookTitle_10500", author, Set.of(genre));
        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(UPDATE_BOOK_TITLE));
        var tempBook = mongoTemplate.findOne(query, Book.class);

        var expectedBook = new Book(tempBook.getId(), "BookTitle_10500", tempBook.getAuthor(), tempBook.getGenres());

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("title").is(DELETE_BOOK_TITLE));
        var bookToDelete = mongoTemplate.findOne(queryBook, Book.class);

        Query queryComments = new Query();
        queryComments.addCriteria(Criteria.where("book").is(bookToDelete));
        var comments = mongoTemplate.find(queryComments, Comment.class);

        Assertions.assertEquals(COUNT_COMMENTS_BOOK_1, comments.size());

        assertThat(bookRepository.findById(bookToDelete.getId())).isPresent();
        bookRepository.deleteById(bookToDelete.getId());
        assertThat(bookRepository.findById(bookToDelete.getId())).isEmpty();

        comments = mongoTemplate.find(queryComments, Comment.class);

        Assertions.assertEquals(0, comments.size());
    }
}