package ru.otus.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.dto.AuthorDto;
import ru.otus.services.AuthorService;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthorRestController.class)
@ContextConfiguration(classes = {AuthorRestController.class})
public class AuthorRestControllerTest {
    @MockBean
    AuthorService authorService;

    @Autowired
    private WebTestClient webTestClient;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectAuthorList() {
        when(authorService.findAll()).thenReturn(Flux.fromIterable(dataProvider.getAuthorDtoList()));

        List<AuthorDto> result = webTestClient
                .get().uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(3)
                .returnResult()
                .getResponseBody();

        verify(authorService).findAll();
        assertThat(result).containsExactlyElementsOf(dataProvider.getAuthorDtoList());
    }
}