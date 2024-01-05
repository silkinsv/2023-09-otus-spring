package ru.otus.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.BookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.services.BookService;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    final DataProvider dataProvider = new DataProvider();

    @Test
    void shouldReturnCorrectBookList() throws Exception {
        List<BookDto> bookList = dataProvider.getBookDtoList();
        given(bookService.findAll()).willReturn(bookList);
        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookList)));
    }

    @Test
    void shouldCreateBook() throws Exception {
        var createBookDto = dataProvider.getCreateBookDto();
        var bookDto = dataProvider.getBookDtoList().get(0);
        given(bookService.create(any())).willReturn(bookDto);
        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createBookDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        var bookDto = dataProvider.getBookDtoList().get(0);
        var updateBookDto = dataProvider.getUpdateBookDto();
        given(bookService.update(any(UpdateBookDto.class))).willReturn(bookDto);
        mvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateBookDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
    }
}
