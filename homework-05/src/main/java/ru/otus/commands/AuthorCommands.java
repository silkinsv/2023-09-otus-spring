package ru.otus.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.converters.AuthorConverter;
import ru.otus.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "abid")
    public String findAuthorById(long id) {
        return authorService.findById(id)
                .map(authorConverter::authorToString)
                .orElse("Book with id %d not found".formatted(id));
    }
}
