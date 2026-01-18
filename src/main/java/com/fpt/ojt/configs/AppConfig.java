package com.fpt.ojt.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;
import java.util.TimeZone;

@Configuration
@EnableAsync
public class AppConfig {

    @Bean
    public LocaleResolver localResolver(@Value("${app.default-locale:vi}") final String defaultLocale,
                                        @Value("${app.default-timezone:Asia/Ho_Chi_Minh}") final String defaultTimezone) {
        AcceptHeaderLocaleResolver localResolver = new AcceptHeaderLocaleResolver();
        localResolver.setDefaultLocale(new Locale.Builder().setLanguage(defaultLocale).build());
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimezone));
        return localResolver;
    }

}
