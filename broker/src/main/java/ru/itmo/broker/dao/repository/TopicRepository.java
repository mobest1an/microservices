package ru.itmo.broker.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
public interface TopicRepository extends JpaRepository<Topic, String> {
}
