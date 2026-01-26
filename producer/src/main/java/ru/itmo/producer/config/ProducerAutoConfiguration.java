package ru.itmo.producer.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.itmo.producer.Producer;

/**
 * @author erik.karapetyan
 */
@Configuration
@ConditionalOnClass(Producer.class)
@EnableConfigurationProperties(BrokerConfigurationProperties.class)
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration")
public class ProducerAutoConfiguration {

}
