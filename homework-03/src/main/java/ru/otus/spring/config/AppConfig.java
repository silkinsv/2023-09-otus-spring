package ru.otus.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "application")
@Data
public class AppConfig implements TestingConfig {
    private final int rightAnswersCountToPass;

    private final Locale locale;
}
