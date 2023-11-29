package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
