package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestingRunnerServiceImpl implements TestingRunnerService {
    private final TestingService testingService;

    private final ResultService resultService;

    @Override
    public void run(Student student) {
        TestResult testResult = testingService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
