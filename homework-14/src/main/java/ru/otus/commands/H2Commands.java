package ru.otus.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@RequiredArgsConstructor
@ShellComponent
public class H2Commands {
    @ShellMethod(value = "Execute h2 console", key = "ec")
    public void executeConsole() throws SQLException {
        Console.main();
    }
}
