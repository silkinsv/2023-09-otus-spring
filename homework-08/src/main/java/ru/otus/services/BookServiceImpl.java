package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book insert(String title, String authorId, List<String> genresIds) {
        var book = new Book(title, getAuthorById(authorId), getGenresByIds(genresIds));
        return save(book);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorId, List<String> genresIds) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id %s not found".formatted(id)));
        var book = new Book(id, title, getAuthorById(authorId), getGenresByIds(genresIds));
        return save(book);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Book save(Book book) {
        return bookRepository.save(book);
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private Set<Genre> getGenresByIds(List<String> genresIds) {
        if (genresIds == null || isEmpty(genresIds)) {
            throw new NotFoundException("Genre list is empty");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != genres.size()) {
            throw new NotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        return new HashSet<>(genres);
    }
}
