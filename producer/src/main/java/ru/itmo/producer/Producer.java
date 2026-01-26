package ru.itmo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import requests.WriteMessageRequest;

/**
 * @author erik.karapetyan
 */
public abstract class Producer<TInput> {

    private final BrokerApiClient brokerApiClient;
    private final String topic;
    private final ObjectMapper objectMapper;

    public Producer(BrokerApiClient brokerApiClient, String topic, ObjectMapper objectMapper) {
        this.brokerApiClient = brokerApiClient;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

    public void produce(TInput input) {
        String content;
        try {
            content = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            content = input.toString();
        }

        brokerApiClient.sendMessage(topic, new WriteMessageRequest(content));
    }
}
