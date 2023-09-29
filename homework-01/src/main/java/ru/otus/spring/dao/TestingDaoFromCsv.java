package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Testing;
import ru.otus.spring.service.LoadTestingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestingDaoFromCsv implements TestingDao {
    private final static String SEPARATOR_QUESTION = ";";
    private final static String SEPARATOR_CORRECT_ANSWER = ",";
    private static final Logger logger = LoggerFactory.getLogger(TestingDaoFromCsv.class);
    private final LoadTestingService service;

    public TestingDaoFromCsv(LoadTestingService service) {
        this.service = service;
    }

    public Testing find() {
        InputStream stream = service.loadTesting();

        if (stream == null) {
            throw new IllegalArgumentException("resource file does not exist!");
        }

        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            List<Question> questions = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                questions.add(generateQuestionFromString(line));
            }

            return new Testing(questions);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Question generateQuestionFromString(String line) {
        String[] elements = line.split(SEPARATOR_QUESTION);
        List<Integer> correctAnswerList = new ArrayList<>();
        String[] correctAnswerStringList = elements[elements.length-1].split(SEPARATOR_CORRECT_ANSWER);

        for(String str : correctAnswerStringList) {
            correctAnswerList.add(Integer.parseInt(str));
        }

        List<Answer> answers = new ArrayList<>();
        for(int i = 1; i< elements.length - 1; i++) {
            Answer answer = new Answer(elements[i], correctAnswerList.contains(i));
            answers.add(answer);
        }
        return new Question(elements[0], answers);
    }
}
