package ru.itmo.broker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.broker.api.dto.responses.ConsumerGroupDto;
import ru.itmo.broker.dao.ConsumerGroupDao;
import ru.itmo.broker.model.ConsumerGroup;

/**
 * @author erik.karapetyan
 */

@Service
@RequiredArgsConstructor
public class ConsumerGroupService {

    private final ConsumerGroupDao consumerGroupDao;
    private final PartitionRouter partitionRouter;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ConsumerGroupDto join(String groupId, String topic) {
        ConsumerGroup consumerGroup = consumerGroupDao.findByGroupIdAndTopic(groupId, topic);
        int clientId = partitionRouter.joinConsumerGroup(consumerGroup);
        return ConsumerGroupDto.fromModel(consumerGroupDao.save(consumerGroup), clientId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ConsumerGroupDto leave(String groupId, String topic, int clientId) {
        ConsumerGroup consumerGroup = consumerGroupDao.findByGroupIdAndTopic(groupId, topic);
        partitionRouter.leaveConsumerGroup(consumerGroup, clientId);
        return ConsumerGroupDto.fromModel(consumerGroupDao.save(consumerGroup), clientId);
    }
}
