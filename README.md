# Custom Spring Boot Starter

## Описание

Этот проект представляет собой кастомный стартер-пакет для Spring Boot, который предоставляет настройку логирования и обработку HTTP-запросов. Стартер позволяет вам легко настраивать логирование, включая уровни логирования и форматирование, а также добавляет фильтр для логирования HTTP-запросов.

## Содержимое проекта

### Структура проекта
```
.
├── README.md
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           ├── App.java
│       │           ├── config
│       │           │   ├── CustomLoggingAutoConfiguration.java
│       │           │   └── CustomLoggingProperties.java
│       │           ├── exception
│       │           │   └── LoggingStartupException.java
│       │           ├── filter
│       │           │   ├── ContentCachingResponseWrapper.java
│       │           │   └── HttpLoggingFilter.java
│       │           └── init
│       │               └── LoggingEnvironmentPostProcessor.java
│       └── resources
│           ├── META-INF
│           │   ├── spring
│           │   │   └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│           │   └── spring.factories
│           └── application.yaml
```

### Основные компоненты

- **`CustomLoggingAutoConfiguration.java`**: Автоконфигурация для настройки кастомного логирования. Автоматически настраивает логирование в соответствии с параметрами, указанными в `application.yaml`.

- **`CustomLoggingProperties.java`**: Класс для привязки свойств логирования из `application.yaml`. Определяет, как загружать и использовать настройки логирования.

- **`LoggingEnvironmentPostProcessor.java`**: Обработчик, который используется для инициализации параметров логирования на уровне окружения.

- **`HttpLoggingFilter.java`**: Фильтр, который логирует HTTP-запросы и ответы. Полезен для отладки и мониторинга.

- **`ContentCachingResponseWrapper.java`**: Обертка для кэширования HTTP-ответов, что позволяет логировать полные ответы (если это необходимо).

## Установка

Сборка и установка артефакта в локальный maven-репозиторий происходит с помощью команды: `mvn clean install`.

Добавьте следующую зависимость в ваш `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>CustomSpringStarter</artifactId>
    <version>1.0-SNAPSHOT</version> 
</dependency>
```

### Конфигурация

Настройте параметры логирования в application.yaml:

```yaml
logging:
  enabled: true
  level:
    root: INFO
    com.example: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### Параметры конфигурации

- `logging.enabled`: Включает или отключает кастомное логирование. Если установлено в true, логирование будет активировано.
- `logging.level`: Определяет уровни логирования для различных пакетов. Поддерживаемые уровни: TRACE, DEBUG, INFO, WARN, ERROR.
- `logging.pattern.console`: Определяет формат логов для консоли. Используйте паттерны из Logback.


## Использование

### Автоматическая конфигурация

При добавлении стартер-пакета в проект, конфигурация логирования автоматически применится в соответствии с параметрами, указанными в application.yaml.

### Фильтр HTTP-запросов

Фильтр HttpLoggingFilter будет автоматически применяться к HTTP-запросам и ответам. Он логирует детали запросов и ответов, что позволяет отслеживать активность и отлаживать приложение.

### Тестирование

Для тестирования конфигурации логирования вы можете использовать следующий тестовый класс:

```java
package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties(CustomLoggingProperties.class)
public class CustomLoggingPropertiesTest {

    @Autowired
    private CustomLoggingProperties customLoggingProperties;

    @Test
    public void testLoggingProperties() {
        assertThat(customLoggingProperties.isEnabled()).isTrue();
        assertThat(customLoggingProperties.getLevel()).isEqualTo("DEBUG");
        assertThat(customLoggingProperties.getPattern()).isEqualTo("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
    }
}
```


