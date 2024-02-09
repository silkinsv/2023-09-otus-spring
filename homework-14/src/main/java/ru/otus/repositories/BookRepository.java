package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.documents.BookDocument;

public interface BookRepository extends MongoRepository<BookDocument, String> {
}
