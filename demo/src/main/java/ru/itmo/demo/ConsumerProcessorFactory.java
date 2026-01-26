package ru.itmo.demo;

import ru.itmo.consumer.BrokerApiClient;
import ru.itmo.consumer.QueueProcessorBase;

public class ConsumerProcessorFactory {

    private final BrokerApiClient brokerApiClient;
    private final ConsumerProperties consumerProperties;

    public ConsumerProcessorFactory(BrokerApiClient brokerApiClient, ConsumerProperties consumerProperties) {
        this.brokerApiClient = brokerApiClient;
        this.consumerProperties = consumerProperties;
    }

    public QueueProcessorBase create() {
        return new DemoConsumer(
                consumerProperties.getTopic(),
                consumerProperties.getGroupId(),
                brokerApiClient
        );
    }
}

