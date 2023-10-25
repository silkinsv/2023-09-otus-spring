package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingServiceImpl implements TestingService {
    private final QuestionDao questionDao;

    private final IOService ioService;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.println("");
        ioService.printFormattedLine(localizedMessagesService.getMessage("message.welcome"));
        List<Question> questions = questionDao.getAll();
        TestResult testResult = new TestResult(student);

        for (Question question : questions) {
            boolean isAnswerValid = getAnswerResult(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean getAnswerResult(Question question) {
        List<Integer> studentsAnswer = new ArrayList<>();
        List<Integer> correctAnswer = new ArrayList<>();
        ioService.println(question.getText());
        Integer i = 1;
        for (Answer answer : question.getAnswers()) {
            ioService.println(i + ". " + answer.getText());
            if (answer.isCorrect()) {
                correctAnswer.add(i);
            }
            i++;
        }
        studentsAnswer = ioService.readListIntForRange(1, question.getAnswers().size()
                , "Error read answer, please try again");
        return new HashSet<>(studentsAnswer).containsAll(correctAnswer)
                && new HashSet<>(correctAnswer).containsAll(studentsAnswer);
    }
}
