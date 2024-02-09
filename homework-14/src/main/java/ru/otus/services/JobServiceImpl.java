package ru.otus.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobServiceImpl implements JobService {
    private Long executionId;

    private final JobOperator jobOperator;

    @Override
    public void run() throws Exception {
        if (executionId != null) {
            throw new IllegalArgumentException("Задача уже запущена");
        }
        Properties properties = new Properties();
        executionId = jobOperator.start("importLibraryJob", properties);
        log.info("Задача \"{}\" выполнена", jobOperator.getSummary(executionId));
    }

    @Override
    public String showJobInfo() throws Exception {
        return jobOperator.getSummary(executionId);
    }

}
