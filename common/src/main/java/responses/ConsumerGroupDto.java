package responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @author erik.karapetyan
 */
@Getter
public class ConsumerGroupDto {

    @JsonProperty("groupId")
    private final String groupId;
    @JsonProperty("topicName")
    private final String topicName;
    @JsonProperty("partitionCount")
    private final int partitionCount;
    @JsonProperty("clientId")
    private final Integer clientId;

    @JsonCreator
    public ConsumerGroupDto(
            @JsonProperty("groupId")
            String groupId,
            @JsonProperty("topicName")
            String topicName,
            @JsonProperty("partitionCount")
            int partitionCount,
            @JsonProperty("clientId")
            Integer clientId
    ) {
        this.groupId = groupId;
        this.topicName = topicName;
        this.partitionCount = partitionCount;
        this.clientId = clientId;
    }
}
