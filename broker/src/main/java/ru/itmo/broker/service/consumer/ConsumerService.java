package ru.itmo.broker.service.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import responses.MessageDto;
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
    public Optional<MessageDto> readMessage(String topic, String groupId, int clientId) {
        ConsumerGroup consumerGroup = consumerGroupDao.findByGroupIdAndTopic(groupId, topic);
        int partition = partitionRouter.getPartitionForClient(consumerGroup, clientId);
        long maxOffset = consumerGroup.getTopic().getOffsets().get(partition);

        Map<Integer, Long> clientOffsets = consumerGroup.getClientOffsets();
        if (clientOffsets == null) {
            clientOffsets = new HashMap<>();
        }

        Long msgOffset = clientOffsets.get(partition);
        if (msgOffset == null) {
            msgOffset = maxOffset;
        }

        Optional<Message> message = messageRepository.findByTopicAndPartitionAndMsgOffset(consumerGroup.getTopic(), partition, msgOffset);
        return message.map(Message::fromModel);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void commitMessage(UUID id, String groupId) {
        Message message = messageRepository.findById(id).orElseThrow();
        ConsumerGroup consumerGroup = consumerGroupDao.findByGroupIdAndTopic(groupId, message.getTopic());

        Map<Integer, Long> clientOffsets = consumerGroup.getClientOffsets();
        clientOffsets.put(message.getPartition(), message.getMsgOffset() + 1);

        consumerGroup.setClientOffsets(clientOffsets);
        message.setCommited(true);

        messageRepository.save(message);
        consumerGroupDao.save(consumerGroup);
    }
}
