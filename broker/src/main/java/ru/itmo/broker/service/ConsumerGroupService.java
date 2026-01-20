package ru.itmo.broker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.broker.api.dto.responses.ConsumerGroupDto;
import ru.itmo.broker.model.ConsumerGroup;

/**
 * @author erik.karapetyan
 */

@Service
@RequiredArgsConstructor
public class ConsumerGroupService {

    private final PartitionRouter partitionRouter;

    public ConsumerGroupDto join(String groupId, String topic) {
        return partitionRouter.joinConsumerGroup(groupId, topic);
    }

    public ConsumerGroupDto leave(String groupId, String topic, int clientId) {
        return partitionRouter.leaveConsumerGroup(groupId, topic, clientId);
    }
}
