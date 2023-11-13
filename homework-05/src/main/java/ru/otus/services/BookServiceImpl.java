package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, long authorId, List<Long> genresIds) {
        var book = new Book(0L, title, getAuthorById(authorId), getGenresByIds(genresIds));
        return save(book);
    }

    @Override
    public Book update(long id, String title, long authorId, List<Long> genresIds) {
        var book = new Book(id, title, getAuthorById(authorId), getGenresByIds(genresIds));
        return save(book);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Book book) {
        return bookRepository.save(book);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private List<Genre> getGenresByIds(List<Long> genresIds) {
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres)) {
            throw new NotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        return genres;
    }
}
