package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.documents.AuthorDocument;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {
    Optional<AuthorDocument> findFirstByMigrationId(Long migrationId);
}
