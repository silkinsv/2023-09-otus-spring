package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Book;
import ru.otus.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .stream()
                .map(bookMapper::toDto)
                .findFirst()
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
    public Book save(CreateBookDto bookDto) {
        return bookRepository.save(bookMapper.toEntity(bookDto));
    }

    @Override
    @Transactional
    public Book save(UpdateBookDto bookDto) {
        bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId())));
        return bookRepository.save(bookMapper.toEntity(bookDto));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
