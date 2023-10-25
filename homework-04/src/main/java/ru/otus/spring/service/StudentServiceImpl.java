package ru.otus.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;


@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final IOService ioService;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public Student registerStudent() {
        String firstName = null;
        String lastName = null;
        while (firstName == null || firstName.isEmpty()) {
            firstName = ioService.readStringWithPrompt(localizedMessagesService.getMessage("message.first_name"));
        }

        while (lastName == null || lastName.isEmpty()) {
            lastName = ioService.readStringWithPrompt(localizedMessagesService.getMessage("message.last_name"));
        }

        return new Student(firstName, lastName);
    }

}
