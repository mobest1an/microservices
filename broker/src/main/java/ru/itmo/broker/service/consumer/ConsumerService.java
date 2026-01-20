package ru.itmo.broker.service.consumer;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.broker.api.dto.responses.MessageDto;
import ru.itmo.broker.dao.ConsumerGroupDao;
import ru.itmo.broker.dao.repository.MessageRepository;
import ru.itmo.broker.model.ConsumerGroup;
import ru.itmo.broker.model.Message;
import ru.itmo.broker.service.PartitionRouter;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final PartitionRouter partitionRouter;
    private final ConsumerGroupDao consumerGroupDao;
    private final MessageRepository messageRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<MessageDto> readMessage(String topic, String groupId, int clientId, Long offset) {
        ConsumerGroup consumerGroup = consumerGroupDao.findByGroupIdAndTopic(groupId, topic);
        int partition = partitionRouter.getPartitionForClient(consumerGroup, clientId);
        long maxOffset = consumerGroup.getTopic().getOffsets().get(partition);
        if (offset == null) {
            offset = maxOffset;
        }

        Optional<Message> message = messageRepository.findByTopicAndPartitionAndMsgOffset(consumerGroup.getTopic(), partition, offset);
        return message.map(MessageDto::fromModel);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void commitMessage(UUID id) {
        Message message = messageRepository.findById(id).orElseThrow();
        message.setCommited(true);
        messageRepository.save(message);
    }
}
