package ru.itmo.broker.api.dto.responses;

import java.util.UUID;

import lombok.Getter;
import ru.itmo.broker.model.Message;

/**
 * @author erik.karapetyan
 */
@Getter
public class MessageDto {

    private final UUID id;
    private final String content;
    private final int partition;
    private final long msgOffset;
    private final boolean commited;
    private final String topicName;

    private MessageDto(UUID id, String content, int partition, long msgOffset, boolean commited, String topicName) {
        this.id = id;
        this.content = content;
        this.partition = partition;
        this.msgOffset = msgOffset;
        this.commited = commited;
        this.topicName = topicName;
    }

    public static MessageDto fromModel(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getPartition(),
                message.getMsgOffset(),
                message.isCommited(),
                message.getTopic().getName()
        );
    }
}
