package ru.otus.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final IOService ioService;

    @Override
    public Student registerStudent() {
        String firstName = ioService.readStringWithPrompt("Please input your first name");
        String lastName = ioService.readStringWithPrompt("Please input your last name");
        return new Student(firstName, lastName);
    }

}
