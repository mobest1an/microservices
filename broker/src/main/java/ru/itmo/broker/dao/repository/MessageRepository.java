package ru.itmo.broker.dao.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.broker.model.Message;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {

    Optional<Message> findByTopicAndPartitionAndMsgOffset(Topic topic, int partition, long msgOffset);
}
