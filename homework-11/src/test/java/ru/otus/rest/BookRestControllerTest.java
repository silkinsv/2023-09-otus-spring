package ru.otus.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.BookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Book;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(BookRestController.class)
@ContextConfiguration(classes = {BookRestController.class})
public class BookRestControllerTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    GenreRepository genreRepository;

    @MockBean
    BookMapper bookMapper;

    @Autowired
    private WebTestClient webTestClient;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectBookList() {
        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(dataProvider.getBookList()));
        when(bookMapper.toDto(dataProvider.getBookList().get(0))).thenReturn(dataProvider.getBookDtoList().get(0));
        when(bookMapper.toDto(dataProvider.getBookList().get(1))).thenReturn(dataProvider.getBookDtoList().get(1));
        when(bookMapper.toDto(dataProvider.getBookList().get(2))).thenReturn(dataProvider.getBookDtoList().get(2));

        List<BookDto> result = webTestClient
                .get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(3)
                .returnResult()
                .getResponseBody();

        verify(bookRepository).findAll();
        assertThat(result).containsExactlyElementsOf(dataProvider.getBookDtoList());
    }

    @Test
    void shouldCreateBook() {
        Book book = dataProvider.getCreateBook();

        when(bookRepository.save(any())).thenReturn(Mono.just(book));
        when(authorRepository.findById("2L")).thenReturn(Mono.just(dataProvider.getAuthorList().get(1)));
        when(genreRepository.findById("3L")).thenReturn(Mono.just(dataProvider.getGenreList().get(2)));
        when(bookMapper.toDto(dataProvider.getCreateBook())).thenReturn(dataProvider.getNewBook());

        BookDto result = webTestClient
                .post().uri("/api/v1/books")
                .bodyValue(dataProvider.getCreateBookDto())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(dataProvider.getNewBook(), result);
    }

    @Test
    void shouldNotFoundForCreateBook() {
        when(authorRepository.findById("2L")).thenReturn(Mono.error(new NotFoundException("")));
        when(genreRepository.findById("3L")).thenReturn(Mono.error(new NotFoundException("")));

        BookDto result = webTestClient
                .post().uri("/api/v1/books")
                .bodyValue(dataProvider.getCreateBookDto())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertNull(result);
    }

    @Test
    void shouldUpdateBook() {
        Book book = dataProvider.getUpdateBook();

        when(bookRepository.save(any())).thenReturn(Mono.just(book));
        when(authorRepository.findById("1L")).thenReturn(Mono.just(dataProvider.getAuthorList().get(0)));
        when(genreRepository.findById("1L")).thenReturn(Mono.just(dataProvider.getGenreList().get(0)));
        when(bookMapper.toDto(dataProvider.getUpdateBook())).thenReturn(dataProvider.getEditBook());

        BookDto result = webTestClient
                .put().uri("/api/v1/books/{id}", "1L")
                .bodyValue(dataProvider.getUpdateBookDto())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        verify(bookRepository).save(book);
        assertEquals(dataProvider.getEditBook(), result);
    }

    @Test
    void shouldDeleteBook() {
        when(bookRepository.deleteById("1L")).thenReturn(Mono.just(dataProvider.getBookList().get(0)).then());

        webTestClient
                .delete().uri("/api/v1/books/{id}", "1L")
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository).deleteById("1L");
    }
}
