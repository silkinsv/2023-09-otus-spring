package ru.otus.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.models.Book;
import ru.otus.models.Comment;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий на основе mongodb для работы с комментариями ")
@DataMongoTest
public class CommentRepositoryMongoTest {
    private static final String FIRST_COMMENT_TEXT = "Cool";

    private static final String DELETE_COMMENT_TEXT = "Normal";

    private static final String FIRST_BOOK_TITLE = "BookTitle_1";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @Test
    void shouldFindExpectedCommentById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("text").is(FIRST_COMMENT_TEXT));
        var expectedComment = mongoTemplate.findOne(query, Comment.class);

        Comment actualComment = commentRepository.findById(expectedComment.getId()).get();
        assertEquals(expectedComment, actualComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentListByBook() {
        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("title").is(FIRST_BOOK_TITLE));
        var tempBook = mongoTemplate.findOne(queryBook, Book.class);

        Query queryComment = new Query();
        queryComment.addCriteria(Criteria.where("book").is(tempBook));
        var expectedComments = mongoTemplate.find(queryComment, Comment.class);

        var actualComments = commentRepository.findByBookId(tempBook.getId());

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        Query query = new Query();
        query.addCriteria(Criteria.where("text").is(DELETE_COMMENT_TEXT));
        var expectedComment = mongoTemplate.findOne(query, Comment.class);

        assertThat(commentRepository.findById(expectedComment.getId())).isPresent();
        commentRepository.deleteById(expectedComment.getId());
        assertThat(commentRepository.findById(expectedComment.getId())).isEmpty();
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("title").is(FIRST_BOOK_TITLE));
        var tempBook = mongoTemplate.findOne(queryBook, Book.class);

        var expectedComment = new Comment("some_comment", tempBook);
        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        Query query = new Query();
        query.addCriteria(Criteria.where("text").is(FIRST_COMMENT_TEXT));
        var tempComment = mongoTemplate.findOne(query, Comment.class);

        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("title").is(FIRST_BOOK_TITLE));
        var tempBook = mongoTemplate.findOne(queryBook, Book.class);

        var expectedComment = new Comment(tempComment.getId(), "some_comment", System.getProperty("user.name"), Instant.now(), tempBook);

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }
}
