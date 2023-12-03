package ru.otus.services;

import ru.otus.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String id);

    List<Comment> findAllByBookId(String bookId);

    Comment insert(String bookId, String comment);

    Comment update(String id, String comment);

    void deleteById(String id);
}
