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
import ru.otus.services.BookService;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(BookRestController.class)
@ContextConfiguration(classes = {BookRestController.class})
public class BookRestControllerTest {
    @MockBean
    BookService bookService;

    @Autowired
    private WebTestClient webTestClient;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectBookList() {
        when(bookService.findAll()).thenReturn(Flux.fromIterable(dataProvider.getBookDtoList()));

        List<BookDto> result = webTestClient
                .get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(3)
                .returnResult()
                .getResponseBody();

        verify(bookService).findAll();
        assertThat(result).containsExactlyElementsOf(dataProvider.getBookDtoList());
    }

    @Test
    void shouldCreateBook() {

        when(bookService.create(any())).thenReturn(Mono.just(dataProvider.getBookDtoList().get(0)));

        BookDto result = webTestClient
                .post().uri("/api/v1/books")
                .bodyValue(dataProvider.getCreateBookDto())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        assertEquals(dataProvider.getBookDtoList().get(0), result);
    }

    @Test
    void shouldUpdateBook() {
        when(bookService.update(any())).thenReturn(Mono.just(dataProvider.getBookDtoList().get(1)));

        BookDto result = webTestClient
                .put().uri("/api/v1/books/{id}", "1L")
                .bodyValue(dataProvider.getUpdateBookDto())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .returnResult()
                .getResponseBody();

        verify(bookService).update(dataProvider.getUpdateBookDto());
        assertEquals(dataProvider.getBookDtoList().get(1), result);
    }

    @Test
    void shouldDeleteBook() {
        webTestClient
                .delete().uri("/api/v1/books/{id}", "1L")
                .exchange()
                .expectStatus().isOk();

        verify(bookService).deleteById("1L");
    }
}
