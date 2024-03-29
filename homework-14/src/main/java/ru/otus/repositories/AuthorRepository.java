package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.documents.AuthorDocument;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
}
