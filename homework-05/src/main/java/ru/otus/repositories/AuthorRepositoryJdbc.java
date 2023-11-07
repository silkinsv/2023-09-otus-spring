package ru.otus.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJdbc implements AuthorRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    private final AuthorRowMapper authorRowMapper = new AuthorRowMapper();

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query(
                "SELECT id, full_name FROM authors", Collections.emptyMap(), authorRowMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
        Author author = jdbcOperations.queryForObject(
                "SELECT id, full_name FROM authors WHERE id = :id",
                Map.of("id", id),
                authorRowMapper);
        return Optional.ofNullable(author);
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
