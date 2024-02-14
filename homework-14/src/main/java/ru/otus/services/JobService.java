package ru.otus.services;

public interface JobService {
    void run() throws Exception;

    String showJobInfo() throws Exception;
}
