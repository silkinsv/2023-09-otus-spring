package ru.otus.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropDb", author = "silkinsv", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "silkinsv")
    public void insertAuthors(AuthorRepository repository) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("1", "Author_1"));
        authors.add(new Author("2", "Author_2"));
        authors.add(new Author("3", "Author_3"));
        repository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "silkinsv")
    public void insertGenres(GenreRepository repository) {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("1", "Genre_1"));
        genres.add(new Genre("2", "Genre_2"));
        genres.add(new Genre("3", "Genre_3"));
        genres.add(new Genre("4", "Genre_4"));
        genres.add(new Genre("5", "Genre_5"));
        genres.add(new Genre("6", "Genre_6"));
        repository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "silkinsv")
    public void insertBooks(BookRepository bookRepository,
                            AuthorRepository authorRepository,
                            GenreRepository genreRepository) {
        List<Book> books = new ArrayList<>();
        Set<Genre> genres = new HashSet<>();
        var author = authorRepository.findById("1").orElseThrow(() -> new NotFoundException("Author with id 1 not found"));
        genres.add(genreRepository.findById("1").orElseThrow(() -> new NotFoundException("Genre with id 1 not found")));
        genres.add(genreRepository.findById("2").orElseThrow(() -> new NotFoundException("Genre with id 2 not found")));
        books.add(new Book("1", "BookTitle_1", author, genres));

        author = authorRepository.findById("2").orElseThrow(() -> new NotFoundException("Author with id 2 not found"));
        genres.add(genreRepository.findById("3").orElseThrow(() -> new NotFoundException("Genre with id 3 not found")));
        genres.add(genreRepository.findById("4").orElseThrow(() -> new NotFoundException("Genre with id 4 not found")));
        books.add(new Book("2", "BookTitle_2", author, genres));

        author = authorRepository.findById("3").orElseThrow(() -> new NotFoundException("Author with id 3 not found"));
        genres.add(genreRepository.findById("5").orElseThrow(() -> new NotFoundException("Genre with id 5 not found")));
        genres.add(genreRepository.findById("6").orElseThrow(() -> new NotFoundException("Genre with id 6 not found")));
        books.add(new Book("3", "BookTitle_3", author, genres));

        bookRepository.saveAll(books);
    }
}