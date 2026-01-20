package ru.itmo.broker.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.broker.model.Message;

/**
 * @author erik.karapetyan
 */
public interface MessageRepository extends JpaRepository<Message, String> {
}