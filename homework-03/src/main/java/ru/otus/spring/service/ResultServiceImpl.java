package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.TestingConfig;
import ru.otus.spring.domain.TestResult;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {
    private final TestingConfig testingConfig;

    private final IOService ioService;

    private final MessageSource messageSource;

    @Override
    public void showResult(TestResult testResult, Locale locale) {
        ioService.println("");
        ioService.println(messageSource.getMessage("message.result"
                , null
                , locale));
        ioService.println(messageSource.getMessage("message.student"
                , new String[]{testResult.getStudent().getFullName()}
                , locale));
        ioService.println(messageSource.getMessage("message.count_questions"
                , new String[]{Integer.toString(testResult.getAnsweredQuestions().size())}
                , locale));
        ioService.println(messageSource.getMessage("message.count_right_questions"
                , new String[]{Integer.toString(testResult.getRightAnswersCount())}
                , locale));

        if (testResult.getRightAnswersCount() >= testingConfig.getRightAnswersCountToPass()) {
            ioService.println(messageSource.getMessage("message.pass", null, locale));
            return;
        }
        ioService.println(messageSource.getMessage("message.fail", null, locale));
    }
}
