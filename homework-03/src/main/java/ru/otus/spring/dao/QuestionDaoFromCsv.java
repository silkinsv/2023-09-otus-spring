package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.config.TestFileNameProvider;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.spring.exeption.ResourceCsvEx;

@RequiredArgsConstructor
@Repository
public class QuestionDaoFromCsv implements QuestionDao {
    private static final String SEPARATOR_QUESTION = ";";

    private static final String SEPARATOR_CORRECT_ANSWER = ",";

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionDaoFromCsv.class);

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> getAll() {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName);
             InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream)
                     , StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            List<Question> questions = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                questions.add(generateQuestionFromString(line));
            }

            return questions;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new ResourceCsvEx(e.getMessage(), e);
        }
    }

    private Question generateQuestionFromString(String line) {
        String[] elements = line.split(SEPARATOR_QUESTION);
        List<Integer> correctAnswerList = new ArrayList<>();
        String[] correctAnswerStringList = elements[elements.length - 1].split(SEPARATOR_CORRECT_ANSWER);

        for (String str : correctAnswerStringList) {
            correctAnswerList.add(Integer.parseInt(str));
        }

        List<Answer> answers = new ArrayList<>();
        for (int i = 1; i < elements.length - 1; i++) {
            Answer answer = new Answer(elements[i], correctAnswerList.contains(i));
            answers.add(answer);
        }
        return new Question(elements[0], answers);
    }
}
