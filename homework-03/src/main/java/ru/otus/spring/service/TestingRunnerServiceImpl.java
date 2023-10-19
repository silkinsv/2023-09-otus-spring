package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.AppConfig;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestingRunnerServiceImpl implements TestingRunnerService, CommandLineRunner {
    private final TestingService testingService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final AppConfig appConfig;

    @Override
    public void run() {
        Student student = studentService.registerStudent(appConfig.getLocale());
        TestResult testResult = testingService.executeTestFor(student, appConfig.getLocale());
        resultService.showResult(testResult, appConfig.getLocale());
    }

    @Override
    public void run(String... args) {
        run();
    }
}
