package ru.otus.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(Comment comment) {
        return "Id: %s, login: %s, text: %s, timestamp: %s, book: {%s}".formatted(
                comment.getId(),
                comment.getLogin(),
                comment.getText(),
                comment.getTimestamp().toString(),
                bookConverter.bookToSimpleString(comment.getBook()));
    }

}
