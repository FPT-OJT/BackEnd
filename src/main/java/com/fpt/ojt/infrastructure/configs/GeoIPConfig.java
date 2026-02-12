package com.fpt.ojt.infrastructure.configs;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class GeoIPConfig {

    @Bean
    public DatabaseReader databaseReader() throws Exception {
        InputStream database = getClass()
                .getResourceAsStream("/GeoLite2-City.mmdb");

        return new DatabaseReader.Builder(database).build();
    }
}