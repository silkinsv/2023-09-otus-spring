package ru.otus.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.services.JobService;

@RequiredArgsConstructor
@ShellComponent
public class MigrationCommands {
    private final JobService jobService;

    @ShellMethod(value = "start migration with clear", key = "sm")
    public void startMigrationJobWithClear() throws Exception {
        jobService.run();
    }

    @ShellMethod(value = "Показать информацию о задаче", key = "info")
    public String showJobInfo() throws Exception {
        return jobService.showJobInfo();
    }
}
