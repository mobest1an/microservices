package requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author erik.karapetyan
 */
public record WriteMessageRequest(@JsonProperty("content") String content) {
}
