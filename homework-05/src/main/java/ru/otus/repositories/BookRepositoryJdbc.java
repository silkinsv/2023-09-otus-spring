package ru.otus.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {
    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbcOperations;

    private final BookResultSetExtractor extractor = new BookResultSetExtractor();

    private final BookGenreRowMapper bookGenreRowMapper = new BookGenreRowMapper();

    private final BookRowMapper bookRowMapper = new BookRowMapper();

    @Override
    public Optional<Book> findById(long id) {
        List<Book> found = jdbcOperations.query("SELECT b.id, b.title, b.author_id, a.full_name, bg.genre_id, g.name " +
                "FROM books b " +
                "LEFT JOIN authors a ON a.id = b.author_id " +
                "LEFT JOIN books_genres bg ON bg.book_id = b.id " +
                "LEFT JOIN genres g ON g.id = bg.genre_id " +
                "WHERE b.id = :id"
                , Map.of("id", id)
                , extractor);
        if (found == null || found.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(found.get(0));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbcOperations.query("SELECT b.id, b.title, b.author_id, a.full_name " +
                "FROM books b " +
                "LEFT JOIN authors a on a.id = b.author_id", Collections.emptyMap(), bookRowMapper);
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query(
                "SELECT book_id, genre_id FROM books_genres"
                , Collections.emptyMap()
                , bookGenreRowMapper);
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> mapGenre = genres.stream().collect(Collectors.toMap(Genre::getId, item -> item));
        genres.clear();
        Map<Long, List<Long>> mapBookGenre = relations
                .stream()
                .collect(Collectors.groupingBy(
                        BookGenreRelation::bookId
                        , Collectors.mapping(BookGenreRelation::genreId, Collectors.toList())));
        relations.clear();
        for (Book book : booksWithoutGenres) {
            List<Genre> bookGenres = new ArrayList<>();
            List<Long> genreIdList = mapBookGenre.get(book.getId());
            if (genreIdList != null) {
                genreIdList.forEach(id -> bookGenres.add(mapGenre.get(id)));
            }
            book.setGenres(bookGenres);
            mapBookGenre.remove(book.getId());
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId",book.getAuthor().getId());

        jdbcOperations.update("INSERT INTO books (title, author_id) values (:title, :authorId)"
                , parameters
                , keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        jdbcOperations.update("UPDATE books SET title = :title, author_id = :authorId WHERE id = :id"
                ,Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId(), "id", book.getId()));

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // batchUpdate
        List<BookGenreRelation> bg = new ArrayList<>();
        book.getGenres().forEach(g -> bg.add(new BookGenreRelation(book.getId(), g.getId())));
        jdbcOperations.batchUpdate("INSERT INTO books_genres (book_id, genre_id) VALUES(:bookId, :genreId)"
                , SqlParameterSourceUtils.createBatch(bg));
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcOperations.update("DELETE FROM books_genres WHERE book_id = :id"
                , Map.of("id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            return new Book(id, title, new Author(authorId, fullName), null);
        }
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
                List<Genre> genres;
                Book oldBook = books.get(id);
                if (oldBook != null) {
                    genres = oldBook.getGenres();
                } else {
                    genres = new ArrayList<>();
                }
                Long genreId = rs.getObject("genre_id", Long.class);
                if (genreId != null) {
                    final String genreName = rs.getString("name");
                    final Genre genre = new Genre(genreId, genreName);
                    genres.add(genre);
                }
                Book newBook = new Book(id, title, author, genres);
                books.put(id, newBook);
            }
            return new ArrayList<>(books.values());
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
