package ru.itmo.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.itmo.demo.DemoProducer;
import ru.itmo.producer.BrokerApiClient;
import ru.itmo.producer.config.BrokerConfigurationProperties;

/**
 * @author erik.karapetyan
 */
@Configuration
public class DemoProducerContextConfiguration {

    @Bean("producerBrokerApiClient")
    public BrokerApiClient brokerApiClient(RestTemplate restTemplate, BrokerConfigurationProperties brokerConfigurationProperties) {
        return new BrokerApiClient(restTemplate, brokerConfigurationProperties);
    }

    @Bean
    public DemoProducer demoProducer(BrokerApiClient producerBrokerApiClient) {
        return new DemoProducer(producerBrokerApiClient, "topic", new ObjectMapper());
    }
}
