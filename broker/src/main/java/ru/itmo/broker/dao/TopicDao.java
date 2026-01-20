package ru.itmo.broker.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.itmo.broker.dao.repository.TopicRepository;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
@Repository
@RequiredArgsConstructor
public class TopicDao {

    private final TopicRepository topicRepository;

    public Topic findById(String topicName) {
        return topicRepository.findById(topicName).orElseThrow();
    }

    public void save(Topic topic) {
        topicRepository.save(topic);
    }
}
