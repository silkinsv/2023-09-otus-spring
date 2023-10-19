package ru.otus.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final IOService ioService;

    private final MessageSource messageSource;

    @Override
    public Student registerStudent(Locale locale) {
        String firstName = ioService.readStringWithPrompt(messageSource.getMessage("message.first_name", null, locale));
        String lastName = ioService.readStringWithPrompt(messageSource.getMessage("message.last_name", null, locale));
        return new Student(firstName, lastName);
    }

}
