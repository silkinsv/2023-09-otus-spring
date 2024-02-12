package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.documents.GenreDocument;

public interface GenreRepository extends MongoRepository<GenreDocument, String> {
}
