package ru.otus.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "application")
@Data
public class AppConfig implements TestingConfig, TestFileNameProvider, LocaleProvider {
    private final int rightAnswersCountToPass;

    private final Map<String, String> testFileNameByLocale;

    private final Locale locale;

    @Override
    public String getTestFileName() {
        return testFileNameByLocale.get(locale.toLanguageTag());
    }
}
