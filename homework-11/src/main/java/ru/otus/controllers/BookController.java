package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookController {
    @GetMapping({"/", "/books", "/books/"})
    public String getBookListPage() {
        return "book-list";
    }

    @GetMapping("/books/add")
    public String addBookPage() {
        return "book-create";
    }

    @GetMapping("/books/{id}/edit")
    public String editBookPage(@PathVariable("id") String id) {
        return "book-update";
    }
}
