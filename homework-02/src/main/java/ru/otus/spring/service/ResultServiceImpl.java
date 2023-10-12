package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.TestingConfig;
import ru.otus.spring.domain.TestResult;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {
    private final TestingConfig testingConfig;
    private final IOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.println("");
        ioService.println("Test results: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        ioService.printFormattedLine("Answered questions count: %d", testResult.getAnsweredQuestions().size());
        ioService.printFormattedLine("Right answers count: %d", testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testingConfig.getRightAnswersCountToPass()) {
            ioService.println("Congratulations! You passed test!");
            return;
        }
        ioService.println("Sorry. You fail test.");
    }
}
