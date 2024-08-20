package com.example.config;

import com.example.filter.HttpLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@EnableConfigurationProperties(CustomLoggingProperties.class)
public class CustomLoggingAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingAutoConfiguration.class);

    private final Environment environment;

    public CustomLoggingAutoConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnProperty(name = "logging.enabled", havingValue = "true", matchIfMissing = true)
    public HttpLoggingFilter httpLoggingFilter(CustomLoggingProperties properties) {
        boolean loggingEnabled = Boolean.parseBoolean(environment.getProperty("logging.enabled", "true"));
        if (!loggingEnabled) {
            logger.info("Logging is disabled");
            return null;
        }

        logger.info("Initializing HttpLoggingFilter with level {} and pattern {}", properties.getLevel(), properties.getPattern());
        return new HttpLoggingFilter();
    }
}
