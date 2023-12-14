package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.BookDto;
import ru.otus.dto.SaveBookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;
import ru.otus.services.utils.DtoConverter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id)
                .stream()
                .map(DtoConverter::convertToBookDto)
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(DtoConverter::convertToBookDto)
                .toList();
    }

    @Override
    @Transactional
    public Book save(SaveBookDto bookDto) {
        if (bookDto.getId() == null) {
            return insert(bookDto);
        }
        return update(bookDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book insert(SaveBookDto bookDto) {
        var book = new Book(null,
                bookDto.getTitle(),
                getAuthorById(bookDto.getAuthorId()),
                getGenresByNames(bookDto.getGenres()));
        return bookRepository.save(book);
    }

    private Book update(SaveBookDto bookDto) {
        bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId())));
        var book = new Book(bookDto.getId()
                , bookDto.getTitle()
                , getAuthorById(bookDto.getAuthorId())
                , getGenresByNames(bookDto.getGenres()));
        return bookRepository.save(book);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Set<Genre> getGenresByIds(List<Long> genresIds) {
        if (genresIds == null || isEmpty(genresIds)) {
            throw new NotFoundException("Genre list is empty");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != genres.size()) {
            throw new NotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        return new HashSet<>(genres);
    }

    private Set<Genre> getGenresByNames(String genresString) {
        if (genresString == null || genresString.isEmpty()) {
            throw new NotFoundException("Genre list is empty");
        }

        List<String> genreList = new ArrayList<>();
        for (String genre : genresString.split(",")) {
            genre = genre.replace('[', ' ').replace(']', ' ').trim();
            genreList.add(genre);
        }

        var genres = genreRepository.findAllByNameIn(genreList);
        if (genreList.size() != genres.size()) {
            throw new NotFoundException("Genres with names %s not found".formatted(genreList));
        }
        return new HashSet<>(genres);
    }
}
