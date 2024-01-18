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
import ru.otus.mappers.GenreMapper;
import ru.otus.repositories.GenreRepository;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(GenreRestController.class)
@ContextConfiguration(classes = {GenreRestController.class})
public class GenreRestControllerTest {
    @MockBean
    GenreRepository genreRepository;

    @MockBean
    GenreMapper genreMapper;

    @Autowired
    private WebTestClient webTestClient;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectGenreList() {
        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(dataProvider.getGenreList()));
        when(genreMapper.toDto(dataProvider.getGenreList().get(0))).thenReturn(dataProvider.getGenreDtoList().get(0));
        when(genreMapper.toDto(dataProvider.getGenreList().get(1))).thenReturn(dataProvider.getGenreDtoList().get(1));
        when(genreMapper.toDto(dataProvider.getGenreList().get(2))).thenReturn(dataProvider.getGenreDtoList().get(2));
        when(genreMapper.toDto(dataProvider.getGenreList().get(3))).thenReturn(dataProvider.getGenreDtoList().get(3));
        when(genreMapper.toDto(dataProvider.getGenreList().get(4))).thenReturn(dataProvider.getGenreDtoList().get(4));
        when(genreMapper.toDto(dataProvider.getGenreList().get(5))).thenReturn(dataProvider.getGenreDtoList().get(5));

        List<GenreDto> result = webTestClient
                .get().uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(6)
                .returnResult()
                .getResponseBody();

        verify(genreRepository).findAll();
        assertThat(result).containsExactlyElementsOf(dataProvider.getGenreDtoList());
    }
}
