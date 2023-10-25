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

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.println("");
        ioService.println(localizedMessagesService.getMessage("message.result"));
        ioService.println(localizedMessagesService.getMessage("message.student"
                , new String[]{testResult.getStudent().getFullName()}));
        ioService.println(localizedMessagesService.getMessage("message.count_questions"
                , new String[]{Integer.toString(testResult.getAnsweredQuestions().size())}));
        ioService.println(localizedMessagesService.getMessage("message.count_right_questions"
                , new String[]{Integer.toString(testResult.getRightAnswersCount())}));

        if (testResult.getRightAnswersCount() >= testingConfig.getRightAnswersCountToPass()) {
            ioService.println(localizedMessagesService.getMessage("message.pass"));
            return;
        }
        ioService.println(localizedMessagesService.getMessage("message.fail"));
    }
}
