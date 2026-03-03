package com.fpt.ojt.infrastructure.configs;

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableAsync
public class AppConfig {

    @Bean
    public LocaleResolver localResolver(
            @Value("${app.default-locale:vi}") final String defaultLocale,
            @Value("${app.default-timezone:Asia/Ho_Chi_Minh}") final String defaultTimezone) {
        AcceptHeaderLocaleResolver localResolver = new AcceptHeaderLocaleResolver();

        // Parse locale string (handles both "en" and "en_US" formats)
        String[] parts = defaultLocale.split("_");
        Locale.Builder builder = new Locale.Builder();
        if (parts.length >= 1) {
            builder.setLanguage(parts[0]);
        }
        if (parts.length >= 2) {
            builder.setRegion(parts[1]);
        }
        localResolver.setDefaultLocale(builder.build());

        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimezone));
        return localResolver;
    }

    @Bean(name = "taskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
