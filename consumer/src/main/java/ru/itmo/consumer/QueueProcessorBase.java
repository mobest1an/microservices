package ru.itmo.consumer;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import responses.ConsumerGroupDto;
import responses.MessageDto;

/**
 * @author erik.karapetyan
 */
public abstract class QueueProcessorBase implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueueProcessorBase.class);

    private final String topic;
    private final String groupId;
    private final BrokerApiClient brokerApiClient;
    private Integer clientId = null;

    public QueueProcessorBase(String topic, String groupId, BrokerApiClient brokerApiClient) {
        this.topic = topic;
        this.groupId = groupId;
        this.brokerApiClient = brokerApiClient;
    }

    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 10000));
        joinConsumerGroup();
        while (!Thread.currentThread().isInterrupted()) {
            tryProcess();
        }
    }

    protected void tryProcess() {
        try {
            MessageDto messageDto = readMessage();
            if (messageDto != null) {
                process(messageDto);
                commitMessage(messageDto.getId());
            }
        } catch (Exception e) {
            logger.error("Something went wrong", e);
        }
    }

    protected abstract void process(MessageDto data);

    private void joinConsumerGroup() {
        ConsumerGroupDto consumerGroupDto = brokerApiClient.joinConsumerGroup(topic, groupId);
        clientId = consumerGroupDto.getClientId();
    }

    private MessageDto readMessage() {
        return brokerApiClient.readMessage(topic, groupId, clientId);
    }

    private void commitMessage(UUID id) {
        brokerApiClient.commitMessage(id, groupId);
    }
}
