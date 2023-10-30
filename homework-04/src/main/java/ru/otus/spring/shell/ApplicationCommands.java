package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.domain.Student;
import ru.otus.spring.service.StudentService;
import ru.otus.spring.service.TestingRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {
    private final StudentService studentService;

    private final TestingRunnerService testingRunnerService;

    private Student student;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public void login() {
        this.student = studentService.registerStudent();
    }

    @ShellMethod(value = "Start testing", key = {"s", "start"})
    @ShellMethodAvailability(value = "isStartAvailable")
    public void start() {
        testingRunnerService.run(student);
    }

    private Availability isStartAvailable() {
        return student == null ? Availability.unavailable("Сначала залогиньтесь") : Availability.available();
    }
}
