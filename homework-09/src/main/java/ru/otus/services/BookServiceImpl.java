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

import java.util.List;
import java.util.Set;

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
    public Book create(CreateBookDto bookDto) {
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByIds(bookDto.getGenreId());
        return bookRepository.save(bookMapper.toEntity(bookDto, author, genres));
    }

    @Override
    @Transactional
    public Book update(UpdateBookDto bookDto) {
        bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId())));
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByIds(bookDto.getGenreId());
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

    private Set<Genre> getGenresByIds(long genreId) {
        return Set.of(genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(genreId))));
    }
}
