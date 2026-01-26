package ru.itmo.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import ru.itmo.consumer.BrokerApiClient;
import ru.itmo.consumer.ConsumerManager;
import ru.itmo.consumer.ConsumerProperties;
import ru.itmo.consumer.config.BrokerConfigurationProperties;
import ru.itmo.demo.ConsumerProcessorFactory;

/**
 * @author erik.karapetyan
 */
@Configuration
@EnableRetry(proxyTargetClass = true)
public class DemoConsumerContextConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("consumerBrokerApiClient")
    public BrokerApiClient brokerApiClient(RestTemplate restTemplate, BrokerConfigurationProperties brokerConfigurationProperties) {
        return new BrokerApiClient(restTemplate, brokerConfigurationProperties);
    }

    @Bean
    public ConsumerProperties consumerProperties() {
        return new ConsumerProperties("topic", "group", 12);
    }

    @Bean
    public ConsumerProcessorFactory consumerProcessorFactory(
            BrokerApiClient consumerBrokerApiClient,
            ConsumerProperties consumerProperties
    ) {
        return new ConsumerProcessorFactory(consumerBrokerApiClient, consumerProperties);
    }

    @Bean
    public ConsumerManager consumerManager(
            ConsumerProcessorFactory consumerProcessorFactory,
            ConsumerProperties consumerProperties,
            ThreadPoolTaskExecutor consumerTaskExecutor
    ) {
        return new ConsumerManager(consumerProcessorFactory, consumerProperties, consumerTaskExecutor);
    }
}
