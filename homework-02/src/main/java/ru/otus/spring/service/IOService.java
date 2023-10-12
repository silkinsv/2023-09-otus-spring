package ru.otus.spring.service;

import java.util.List;

public interface IOService {
    void println(String line);

    void printFormattedLine(String s, Object... args);

    String readStringWithPrompt(String prompt);

    List<Integer> readListIntForRange(int min, int max, String errorMessage);
}
