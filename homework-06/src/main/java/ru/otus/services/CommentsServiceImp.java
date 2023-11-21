package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exceptions.NotFoundException;
import ru.otus.exceptions.NotPermissionException;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.CommentRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsServiceImp implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Comment> findAllByBookId(long bookId) {
        Book book = getBookById(bookId);
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(long bookId, String text) {
        var comment = new Comment(null, text, System.getProperty("user.name"), Instant.now(), getBookById(bookId));
        return save(comment);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        Comment currentComment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id)));

        if (!currentComment.getLogin().equals(System.getProperty("user.name"))) {
            throw new NotPermissionException("You don`t have permission to update Comment with id %d".formatted(id));
        }

        var comment = new Comment(id, text, System.getProperty("user.name"), Instant.now(), currentComment.getBook());
        return save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    private Book getBookById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
    }
}
