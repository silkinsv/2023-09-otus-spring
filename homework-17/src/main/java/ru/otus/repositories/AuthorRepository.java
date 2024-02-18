package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.models.Author;

@RepositoryRestResource(path = "author")
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
