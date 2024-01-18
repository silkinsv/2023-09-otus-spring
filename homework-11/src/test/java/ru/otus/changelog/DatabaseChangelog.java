package ru.otus.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelog {
    private Map<String, Author> authorMap;

    private Map<String, Genre> genreMap;

    private Map<String, Book> bookMap;

    @ChangeSet(order = "001", id = "dropDb", author = "silkinsv", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "silkinsv")
    public void insertAuthors(AuthorRepositoryNonReactive repository) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Author_1"));
        authors.add(new Author("Author_2"));
        authors.add(new Author("Author_3"));
        authors = repository.saveAll(authors);
        authorMap = authors.stream().collect(Collectors.toMap(Author::getFullName, author -> author));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "silkinsv")
    public void insertGenres(GenreRepositoryNonReactive repository) {
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
    public void insertBooks(BookRepositoryNonReactive repository) {
        List<Book> books = new ArrayList<>();
        books.add(new Book("BookTitle_1", authorMap.get("Author_1"), genreMap.get("Genre_1")));
        books.add(new Book("BookTitle_2", authorMap.get("Author_2"), genreMap.get("Genre_2")));
        books.add(new Book("BookTitle_3", authorMap.get("Author_3"), genreMap.get("Genre_3")));
        books.add(new Book("1L", "BookTitle_4", authorMap.get("Author_1"), genreMap.get("Genre_1")));
        books = repository.saveAll(books);
        bookMap = books.stream().collect(Collectors.toMap(Book::getTitle, book -> book));
    }
}
