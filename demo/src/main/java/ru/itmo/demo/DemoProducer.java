package ru.itmo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itmo.producer.BrokerApiClient;
import ru.itmo.producer.Producer;

/**
 * @author erik.karapetyan
 */
public class DemoProducer extends Producer<String> {

    public DemoProducer(BrokerApiClient brokerApiClient, String topic, ObjectMapper objectMapper) {
        super(brokerApiClient, topic, objectMapper);
    }
}
