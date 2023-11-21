package ru.otus.repositories;

import ru.otus.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

    void deleteById(long id);
}
