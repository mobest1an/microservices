package ru.itmo.demo;

import responses.MessageDto;
import ru.itmo.consumer.BrokerApiClient;
import ru.itmo.consumer.QueueProcessorBase;

/**
 * @author erik.karapetyan
 */
public class DemoConsumer extends QueueProcessorBase {

    public DemoConsumer(String topic, String groupId, BrokerApiClient brokerApiClient) {
        super(topic, groupId, brokerApiClient);
    }

    @Override
    protected void process(MessageDto data) {
        System.out.println(data + " in thread " + Thread.currentThread().getName());
    }
}
