package ru.itmo.broker.service.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.broker.api.dto.responses.ConsumerGroupDto;
import ru.itmo.broker.dao.ConsumerGroupDao;
import ru.itmo.broker.dao.TopicDao;
import ru.itmo.broker.model.ConsumerGroup;
import ru.itmo.broker.model.Topic;
import ru.itmo.broker.service.PartitionRouter;

/**
 * @author erik.karapetyan
 */

@Service
@RequiredArgsConstructor
public class ConsumerGroupService {

    private final ConsumerGroupDao consumerGroupDao;
    private final TopicDao topicDao;
    private final PartitionRouter partitionRouter;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ConsumerGroupDto create(String groupId, String topicName) {
        Topic topic = topicDao.findById(topicName);
        return ConsumerGroupDto.fromModel(consumerGroupDao.create(groupId, topic), null);
    }

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
