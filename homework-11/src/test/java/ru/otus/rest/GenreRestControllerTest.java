package ru.otus.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.dto.GenreDto;
import ru.otus.services.GenreService;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(GenreRestController.class)
@ContextConfiguration(classes = {GenreRestController.class})
public class GenreRestControllerTest {
    @MockBean
    GenreService genreService;

    @Autowired
    private WebTestClient webTestClient;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectGenreList() {
        when(genreService.findAll()).thenReturn(Flux.fromIterable(dataProvider.getGenreDtoList()));

        List<GenreDto> result = webTestClient
                .get().uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(6)
                .returnResult()
                .getResponseBody();

        verify(genreService).findAll();
        assertThat(result).containsExactlyElementsOf(dataProvider.getGenreDtoList());
    }
}
