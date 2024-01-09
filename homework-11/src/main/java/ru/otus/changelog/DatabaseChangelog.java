package ru.otus.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelog {
    private Map<String, Author> authorMap;

    private Map<String, Genre> genreMap;

    private Book bookWithComment;

    @ChangeSet(order = "001", id = "dropDb", author = "silkinsv", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "silkinsv")
    public void insertAuthors(AuthorRepository repository) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Author_1"));
        authors.add(new Author("Author_2"));
        authors.add(new Author("Author_3"));
        authors = repository.saveAll(authors);
        authorMap = authors.stream().collect(Collectors.toMap(Author::getFullName, author -> author));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "silkinsv")
    public void insertGenres(GenreRepository repository) {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Genre_1"));
        genres.add(new Genre("Genre_2"));
        genres.add(new Genre("Genre_3"));
        genres.add(new Genre("Genre_4"));
        genres.add(new Genre("Genre_5"));
        genres.add(new Genre("Genre_6"));
        genres = repository.saveAll(genres);
        genreMap = genres.stream().collect(Collectors.toMap(Genre::getName, genre -> genre));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "silkinsv")
    public void insertBooks(BookRepository repository) {
        List<Book> books = new ArrayList<>();
        Set<Genre> genres = new HashSet<>();
        genres.add(genreMap.get("Genre_1"));
        genres.add(genreMap.get("Genre_2"));
        books.add(new Book("BookTitle_1", authorMap.get("Author_1"), new HashSet<>(genres)));
        genres.clear();

        genres.add(genreMap.get("Genre_3"));
        genres.add(genreMap.get("Genre_4"));
        books.add(new Book("BookTitle_2", authorMap.get("Author_2"), new HashSet<>(genres)));
        genres.clear();

        genres.add(genreMap.get("Genre_5"));
        genres.add(genreMap.get("Genre_6"));
        books.add(new Book("BookTitle_3", authorMap.get("Author_3"), new HashSet<>(genres)));
        genres.clear();

        bookWithComment = repository.saveAll(books).get(0);
    }
}