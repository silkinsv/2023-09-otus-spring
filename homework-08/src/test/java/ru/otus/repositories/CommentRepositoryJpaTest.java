package ru.otus.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.models.Book;
import ru.otus.models.Comment;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
public class CommentRepositoryJpaTest {
    private static final long FIRST_COMMENT_ID = 1L;

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private CommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @Test
    void shouldFindExpectedCommentById() {
        Comment expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);
        Comment actualComment = repositoryJpa.findById(FIRST_COMMENT_ID).get();
        assertEquals(expectedComment, actualComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentListByBook() {
        TypedQuery<Comment> query = em.getEntityManager().createQuery("select c from Comment c where c.book.id = :bookId ", Comment.class);
        query.setParameter("bookId", FIRST_BOOK_ID);

        var expectedComments = query.getResultList();
        var actualComments = repositoryJpa.findByBookId(FIRST_BOOK_ID);

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(repositoryJpa.findById(FIRST_COMMENT_ID)).isPresent();
        repositoryJpa.deleteById(FIRST_COMMENT_ID);
        assertThat(repositoryJpa.findById(FIRST_COMMENT_ID)).isEmpty();
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(null, "some_comment", System.getProperty("user.name"), Instant.now(), em.find(Book.class, FIRST_COMMENT_ID));
        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(1L, "some_comment", System.getProperty("user.name"), Instant.now(), em.find(Book.class, FIRST_BOOK_ID));

        assertThat(repositoryJpa.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repositoryJpa.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }
}
