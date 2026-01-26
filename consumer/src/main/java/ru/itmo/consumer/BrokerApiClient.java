package ru.itmo.consumer;

import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import responses.ConsumerGroupDto;
import responses.MessageDto;
import ru.itmo.consumer.config.BrokerConfigurationProperties;

/**
 * @author erik.karapetyan
 */
public class BrokerApiClient {

    private static final Logger logger = LoggerFactory.getLogger(BrokerApiClient.class);

    private final RestTemplate restTemplate;
    private final BrokerConfigurationProperties brokerProps;

    public BrokerApiClient(RestTemplate restTemplate, BrokerConfigurationProperties brokerConfigurationProperties) {
        this.restTemplate = restTemplate;
        this.brokerProps = brokerConfigurationProperties;
    }

    @Retryable(
            value = {
                    Exception.class,
            },
            maxAttemptsExpression = "#{${broker.retry.maxAttempts:100}}",
            backoff = @Backoff(
                    delayExpression = "#{${broker.retry.initialDelayMs:2000}}",
                    multiplier = 2.0,
                    maxDelayExpression = "#{${broker.retry.maxDelayMs:30000}}"
            )
    )
    public ConsumerGroupDto joinConsumerGroup(String topic, String groupId) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getGroupsBaseUrl())
                .pathSegment(groupId, "join")
                .queryParam("topic", topic)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ConsumerGroupDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ConsumerGroupDto.class
        );

        logger.info("{} --- {}", response, Thread.currentThread().getName());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("POST " + url + " failed with status " + response.getStatusCode());
        }

        return response.getBody();
    }

    public MessageDto readMessage(String topic, String groupId, int clientId) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getMessageBaseUrl())
                .queryParam("topic", topic)
                .queryParam("groupId", groupId)
                .queryParam("clientId", clientId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<MessageDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                MessageDto.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("POST " + url + " failed with status " + response.getStatusCode());
        }
        return response.getBody();
    }

    public void commitMessage(UUID id, String groupId) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getMessageBaseUrl() + "/commit")
                .queryParam("id", id.toString())
                .queryParam("groupId", groupId)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("POST " + url + " failed with status " + response.getStatusCode());
        }
    }
}
