package ru.itmo.broker.service.producer;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import requests.WriteMessageRequest;
import responses.MessageDto;
import ru.itmo.broker.dao.TopicDao;
import ru.itmo.broker.dao.repository.MessageRepository;
import ru.itmo.broker.model.Message;
import ru.itmo.broker.model.Topic;
import ru.itmo.broker.service.PartitionRouter;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class ProducerService {

    private final PartitionRouter partitionRouter;
    private final TopicDao topicDao;
    private final MessageRepository messageRepository;
    private final SyntaxChecker syntaxChecker;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MessageDto writeMessage(String topicName, WriteMessageRequest request) {
        Topic topic = topicDao.findById(topicName);
        int partition = partitionRouter.getPartitionForProducer(topic);

        long currentOffset = topic.getOffsets().get(partition) + 1;
        topic.getOffsets().replace(partition, currentOffset);

        String header = syntaxChecker.check(request.content());

        Message message = new Message(
                UUID.randomUUID(),
                request.content(),
                header,
                partition,
                currentOffset,
                false,
                topic
        );

        topicDao.save(topic);
        return messageRepository.save(message).fromModel();
    }
}
