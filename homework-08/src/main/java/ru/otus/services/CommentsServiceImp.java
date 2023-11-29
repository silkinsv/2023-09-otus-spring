package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.CommentRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsServiceImp implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Comment> findAllByBookId(String bookId) {
        Book book = getBookById(bookId);
        return commentRepository.findByBook_Id(book.getId());
    }

    @Override
    @Transactional
    public Comment insert(String bookId, String text) {
        var comment = new Comment(text, getBookById(bookId));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(String id, String text) {
        Comment currentComment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %s not found".formatted(id)));
        currentComment.setText(text);
        return commentRepository.save(currentComment);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Book getBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id %s not found".formatted(bookId)));
    }
}
