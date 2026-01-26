package ru.itmo.demo;

import lombok.RequiredArgsConstructor;
import ru.itmo.consumer.AbstractProcessorFactory;
import ru.itmo.consumer.BrokerApiClient;
import ru.itmo.consumer.ConsumerProperties;
import ru.itmo.consumer.QueueProcessorBase;

@RequiredArgsConstructor
public class ConsumerProcessorFactory extends AbstractProcessorFactory {

    private final BrokerApiClient brokerApiClient;
    private final ConsumerProperties consumerProperties;

    public QueueProcessorBase create() {
        return new DemoConsumer(
                consumerProperties.getTopic(),
                consumerProperties.getGroupId(),
                brokerApiClient
        );
    }
}

