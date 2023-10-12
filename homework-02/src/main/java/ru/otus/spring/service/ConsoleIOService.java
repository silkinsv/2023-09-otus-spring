package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class ConsoleIOService implements IOService {
    private static final int MAX_ATTEMPTS = 10;

    private final PrintStream printStream;

    private final Scanner scanner;

    public ConsoleIOService(@Value("#{T(System).out}") PrintStream printStream,
                            @Value("#{T(System).in}") InputStream inputStream) {

        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void println(String line) {
        printStream.println(line);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        println(prompt);
        return scanner.nextLine();
    }

    @Override
    public List<Integer> readListIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = scanner.nextLine();
                List<String> stringValueList = List.of(stringValue.trim().split(","));
                if (stringValueList.size() == 0) {
                    throw new IllegalArgumentException();
                }
                return getListIntFromListString(min, max, stringValueList);
            } catch (IllegalArgumentException e) {
                println(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    private List<Integer> getListIntFromListString(int min, int max, List<String> stringList) {
        List<Integer> resultIntList = new ArrayList<>();
        for (String value : stringList) {
            int intValue = Integer.parseInt(value.trim());
            if (intValue < min || intValue > max) {
                throw new IllegalArgumentException();
            }
            resultIntList.add(intValue);
        }
        return resultIntList;
    }
}
