package ru.itmo.broker.dao;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.broker.dao.repository.ConsumerGroupRepository;
import ru.itmo.broker.dao.repository.TopicRepository;
import ru.itmo.broker.model.ConsumerGroup;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
@Repository
@RequiredArgsConstructor
public class ConsumerGroupDao {

    private final ConsumerGroupRepository consumerGroupRepository;
    private final TopicRepository topicRepository;

    public ConsumerGroup create(String groupId, Topic topic) {
        Map<Integer, Integer> clients = new HashMap<>(topic.getPartitionCount());
        for (int i = 0; i < topic.getPartitionCount(); i++) {
            clients.put(i, null);
        }
        ConsumerGroup consumerGroup = new ConsumerGroup(groupId, topic, clients);
        consumerGroupRepository.save(consumerGroup);
        return consumerGroup;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ConsumerGroup findByGroupIdAndTopic(String groupId, String topicName) {
        Topic topic = topicRepository.findById(topicName).orElseThrow();
        return consumerGroupRepository.findByGroupIdAndTopic(groupId, topic).orElseThrow();
    }

    public ConsumerGroup save(ConsumerGroup consumerGroup) {
        return consumerGroupRepository.save(consumerGroup);
    }
}
