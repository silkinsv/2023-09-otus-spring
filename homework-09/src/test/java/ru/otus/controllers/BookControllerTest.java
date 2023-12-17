package ru.otus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.AuthorDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.GenreDto;
import ru.otus.services.AuthorService;
import ru.otus.services.BookService;
import ru.otus.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    public void getBookListPageTest() throws Exception {
        given(bookService.findAll()).willReturn(
                List.of(new BookDto(1L, "BookTitle_1", "Author_1", "[Genre_1, Genre_2]")
                        , new BookDto(2L, "BookTitle_2", "Author_2", "[Genre_3, Genre_4]")
                        , new BookDto(3L, "BookTitle_3", "Author_3", "[Genre_5, Genre_6]"))
        );
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", hasSize(3)));
    }

    @Test
    public void deleteBookPageTest() throws Exception {
        mvc.perform(get("/books/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    public void addBookPageTest() throws Exception {
        List<AuthorDto> authors = List.of(new AuthorDto(1L, "Author_1")
                , new AuthorDto(2L, "Author_2")
                , new AuthorDto(3L, "Author_3"));
        List<GenreDto> genres = List.of(new GenreDto(1L, "Genre_1")
                , new GenreDto(2L, "Genre_2")
                , new GenreDto(3L, "Genre_3")
                , new GenreDto(4L, "Genre_4")
                , new GenreDto(5L, "Genre_5")
                , new GenreDto(6L, "Genre_6"));
        BookDto bookDto = new BookDto();

        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
        given(bookService.findById(1L)).willReturn(bookDto);

        mvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-save"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("genres", hasSize(6)))
                .andExpect(model().attribute("authors", hasSize(3)));
    }

    @Test
    public void saveBookTest() throws Exception {
        mvc.perform(post("/books/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    public void editBookPageTest() throws Exception {
        List<AuthorDto> authors = List.of(new AuthorDto(1L, "Author_1")
                , new AuthorDto(2L, "Author_2")
                , new AuthorDto(3L, "Author_3"));
        List<GenreDto> genres = List.of(new GenreDto(1L, "Genre_1")
                , new GenreDto(2L, "Genre_2")
                , new GenreDto(3L, "Genre_3")
                , new GenreDto(4L, "Genre_4")
                , new GenreDto(5L, "Genre_5")
                , new GenreDto(6L, "Genre_6"));
        BookDto bookDto = new BookDto(1L, "BookTitle_1", "Author_1", "[Genre_1, Genre_2]");

        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
        given(bookService.findById(1L)).willReturn(bookDto);

        mvc.perform(get("/books/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-update"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("genres", hasSize(6)))
                .andExpect(model().attribute("authors", hasSize(3)));
    }
}
