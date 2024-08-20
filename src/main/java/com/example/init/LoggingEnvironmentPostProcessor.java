package com.example.init;

import com.example.exception.LoggingStartupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class LoggingEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("Вызов LoggingEnvironmentPostProcessor");
        String enablePropertyValue = environment.getProperty("logging.enabled");
        boolean isBoolValue = Boolean.TRUE.toString().equals(enablePropertyValue) || Boolean.FALSE.toString().equals(enablePropertyValue);

        if (!isBoolValue) {
            throw new LoggingStartupException("Ошибка при проверке свойства 'custom.logging.enabled' ");
        }
    }
}