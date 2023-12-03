package ru.otus.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.repositories.CommentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentsCascadeDeleteByBookEventsListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        var source = event.getSource();
        var bookId = source.get("_id").toString();
        List<Comment> comments = commentRepository.findByBookId(bookId);
        if (!comments.isEmpty()) {
            commentRepository.deleteAll(comments);
        }
    }
}
