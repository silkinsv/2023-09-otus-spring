package ru.otus.spring.service;

import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadTestingServiceImpl implements LoadTestingService {

    private static final Logger logger = LoggerFactory.getLogger(LoadTestingServiceImpl.class);
    private final String path;

    public LoadTestingServiceImpl(String path) {
        this.path = path;
    }

    public InputStream loadTesting() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (inputStream == null) {
            logger.error("file not found! " + path);
            return null;
        } else {
            return inputStream;
        }
    }
}
