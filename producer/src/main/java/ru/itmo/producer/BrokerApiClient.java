package ru.itmo.producer;

import java.util.Collections;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import requests.WriteMessageRequest;
import responses.MessageDto;
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

    public MessageDto sendMessage(String topic, WriteMessageRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getMessageBaseUrl())
                .queryParam("topic", topic)
                .toUriString();

        ResponseEntity<MessageDto> response = restTemplate.postForEntity(
                url,
                request,
                MessageDto.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("POST " + url + " failed with status " + response.getStatusCode());
        }
        return response.getBody();
    }

    public MessageDto getById(UUID id) {
        String url = UriComponentsBuilder.fromHttpUrl(brokerProps.getMessageBaseUrl())
                .pathSegment(id.toString())
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
}
