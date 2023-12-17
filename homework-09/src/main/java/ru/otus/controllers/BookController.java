package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.dto.AuthorDto;
import ru.otus.dto.GenreDto;
import ru.otus.services.AuthorService;
import ru.otus.services.BookService;
import ru.otus.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping({"/", "/books", "/books/"})
    public String getBookListPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "book-list";
    }

    @GetMapping("/books/{id}/delete")
    public String deleteBookPage(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/add")
    public String addBookPage(Model model) {
        BookDto book = new BookDto();
        fillModel(model, book);
        return "book-save";
    }

    @PostMapping("/books/edit")
    public String saveBook(CreateBookDto book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/books/edit?id={id}")
    public String saveBook(@RequestParam("id") long id, UpdateBookDto book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String editBookPage(@PathVariable("id") Long id, Model model) {
        BookDto book = bookService.findById(id);
        fillModel(model, book);
        return "book-save";
    }

    private void fillModel(Model model, BookDto book) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
    }
}