package ru.itmo.consumer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author erik.karapetyan
 */
@Configuration
@EnableRetry
public class RetryConfiguration {
}
