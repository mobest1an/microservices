package ru.itmo.broker.api.dto.responses;

import lombok.Getter;
import ru.itmo.broker.model.ConsumerGroup;

/**
 * @author erik.karapetyan
 */
@Getter
public class ConsumerGroupDto {

    private final String groupId;
    private final String topicName;
    private final int partitionCount;
    private final Integer clientId;

    private ConsumerGroupDto(String groupId, String topicName, int partitionCount, Integer clientId) {
        this.groupId = groupId;
        this.topicName = topicName;
        this.partitionCount = partitionCount;
        this.clientId = clientId;
    }

    public static ConsumerGroupDto fromModel(ConsumerGroup consumerGroup, Integer clientId) {
        return new ConsumerGroupDto(
                consumerGroup.getGroupId(),
                consumerGroup.getTopic().getName(),
                consumerGroup.getTopic().getPartitionCount(),
                clientId
        );
    }
}
