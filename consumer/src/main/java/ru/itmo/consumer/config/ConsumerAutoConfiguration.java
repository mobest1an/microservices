package ru.itmo.consumer.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import ru.itmo.consumer.QueueProcessorBase;

/**
 * @author erik.karapetyan
 */
@Configuration
@ConditionalOnClass(QueueProcessorBase.class)
@EnableConfigurationProperties(BrokerConfigurationProperties.class)
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration")
@EnableRetry(proxyTargetClass = true)
public class ConsumerAutoConfiguration {

}
