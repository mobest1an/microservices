package ru.itmo.producer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import requests.WriteMessageRequest;
import ru.itmo.producer.config.BrokerConfigurationProperties;

/**
 * @author erik.karapetyan
 */
public class BrokerApiClient {

    private final RestTemplate restTemplate;
    private final BrokerConfigurationProperties brokerProps;

    public BrokerApiClient(RestTemplate restTemplate, BrokerConfigurationProperties brokerProps) {
        this.restTemplate = restTemplate;
        this.brokerProps = brokerProps;
    }

    public void sendMessage(String topic, WriteMessageRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getMessageBaseUrl())
                .queryParam("topic", topic)
                .toUriString();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                request,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("POST " + url + " failed with status " + response.getStatusCode());
        }
    }
}
