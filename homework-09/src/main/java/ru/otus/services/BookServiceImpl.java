package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Book create(CreateBookDto bookDto, String genresString) {
        bookDto.setGenreIds(Arrays.stream(genresString.trim().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet()));
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByIds(bookDto.getGenreIds());
        return bookRepository.save(bookMapper.toEntity(bookDto, author, genres));
    }

    @Override
    @Transactional
    public Book update(UpdateBookDto bookDto, String genresString) {
        bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId())));
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByNames(genresString);
        return bookRepository.save(bookMapper.toEntity(bookDto, author, genres));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Set<Genre> getGenresByIds(Set<Long> genresIds) {
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

        Set<String> genreList = new HashSet<>();
        for (String genre : genresString.split(",")) {
            genre = genre.replace('[', ' ').replace(']', ' ').trim();
            genreList.add(genre);
        }

        var genres = genreRepository.findAllByNameIn(genreList.stream().toList());
        if (genreList.size() != genres.size()) {
            throw new NotFoundException("Genres with names %s not found".formatted(genreList));
        }
        return new HashSet<>(genres);
    }
}
