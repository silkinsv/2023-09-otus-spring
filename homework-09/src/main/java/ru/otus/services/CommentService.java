package ru.otus.services;

import ru.otus.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);

    Comment insert(long bookId, String comment);

    Comment update(long id, String comment);

    void deleteById(long id);
}
