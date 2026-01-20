package ru.itmo.broker.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.broker.model.ConsumerGroup;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
public interface ConsumerGroupRepository extends JpaRepository<ConsumerGroup, String> {

    Optional<ConsumerGroup> findByGroupIdAndTopic(String groupId, Topic topic);
}
