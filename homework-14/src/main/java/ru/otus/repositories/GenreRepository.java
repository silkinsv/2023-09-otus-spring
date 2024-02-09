package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.documents.GenreDocument;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<GenreDocument, String> {
    Optional<GenreDocument> findFirstByMigrationId(Long migrationId);
}
