package ru.itmo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import requests.WriteMessageRequest;
import responses.MessageDto;

/**
 * @author erik.karapetyan
 */
public abstract class Producer<TInput> {

    private final BrokerApiClient brokerApiClient;
    private final String topic;
    private final ObjectMapper objectMapper;

    public Producer(
            BrokerApiClient brokerApiClient,
            String topic,
            ObjectMapper objectMapper
    ) {
        this.brokerApiClient = brokerApiClient;
        this.topic = topic;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void produce(TInput input, boolean synMode) {
        String content;
        try {
            content = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            content = input.toString();
        }

        MessageDto message = brokerApiClient.sendMessage(topic, new WriteMessageRequest(content));

        if (synMode) {
            while (!brokerApiClient.getById(message.getId()).isCommited()) {
                Thread.sleep(1000);
            }
        }
    }
}
