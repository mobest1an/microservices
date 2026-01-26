package responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @author erik.karapetyan
 */
@Getter
public class MessageDto {

    @JsonProperty("id")
    private final UUID id;
    @JsonProperty("content")
    private final String content;
    @JsonProperty("partition")
    private final int partition;
    @JsonProperty("msgOffset")
    private final long msgOffset;
    @JsonProperty("commited")
    private final boolean commited;
    @JsonProperty("topicName")
    private final String topicName;

    @JsonCreator
    public MessageDto(
            @JsonProperty("id")
            UUID id,
            @JsonProperty("content")
            String content,
            @JsonProperty("partition")
            int partition,
            @JsonProperty("msgOffset")
            long msgOffset,
            @JsonProperty("commited")
            boolean commited,
            @JsonProperty("topicName")
            String topicName
    ) {
        this.id = id;
        this.content = content;
        this.partition = partition;
        this.msgOffset = msgOffset;
        this.commited = commited;
        this.topicName = topicName;
    }
}
