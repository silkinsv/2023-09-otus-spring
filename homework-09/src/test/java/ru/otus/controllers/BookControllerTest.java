package ru.otus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.AuthorDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.GenreDto;
import ru.otus.services.AuthorService;
import ru.otus.services.BookService;
import ru.otus.services.GenreService;
import ru.otus.utils.DataProvider;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;

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

    final DataProvider dataProvider = new DataProvider();

    @Test
    public void getBookListPageTest() throws Exception {
        given(bookService.findAll()).willReturn(dataProvider.getBookDtoList());
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", hasSize(3)));
    }

    @Test
    public void deleteBookPageTest() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    public void addBookPageTest() throws Exception {
        List<AuthorDto> authors = dataProvider.getAuthorDtoList();
        List<GenreDto> genres = dataProvider.getGenreDtoList();
        BookDto bookDto = new BookDto();

        given(authorService.findAll()).willReturn(authors);
        given(genreService.findAll()).willReturn(genres);
        given(bookService.findById(1L)).willReturn(bookDto);

        mvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-create"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("genres", hasSize(6)))
                .andExpect(model().attribute("authors", hasSize(3)));
    }

    @Test
    public void saveBookTest() throws Exception {
        CreateBookDto book = dataProvider.getCreateBookDto();
        mvc.perform(post("/books/create").flashAttr("createBookDto", book))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).create(book);
    }

    @Test
    public void editBookPageTest() throws Exception {
        List<AuthorDto> authors = dataProvider.getAuthorDtoList();
        List<GenreDto> genres = dataProvider.getGenreDtoList();
        BookDto bookDto = dataProvider.getBookDtoList().get((0));

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
