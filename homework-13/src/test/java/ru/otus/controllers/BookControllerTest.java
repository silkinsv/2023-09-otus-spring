package ru.otus.controllers;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ExtendWith(SpringExtension.class)
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

    @Before(value = "deleteBookPageTest")
    public void init() {
        Mockito.doNothing().when(bookService).deleteById(any());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void getBookListPageTest() throws Exception {
        given(bookService.findAll()).willReturn(dataProvider.getBookDtoList());
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void deleteBookPageTest() throws Exception {
        mvc.perform(delete("/books/1").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void deleteBookPageTestForNonAuthorizedUser() throws Exception {
        mvc.perform(delete("/books/1").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
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
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void saveBookTest() throws Exception {
        when(bookService.create(any())).thenReturn(dataProvider.getBookList().get(1));
        CreateBookDto book = dataProvider.getCreateBookDto();
        mvc.perform(post("/books/create").flashAttr("createBookDto", book).with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService, times(1)).create(book);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
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
