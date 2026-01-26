package ru.itmo.broker.api;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import requests.WriteMessageRequest;
import responses.MessageDto;
import ru.itmo.broker.service.consumer.ConsumerService;
import ru.itmo.broker.service.producer.ProducerService;

/**
 * @author erik.karapetyan
 */
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final ProducerService producerService;
    private final ConsumerService consumerService;

    @PostMapping
    public void write(@RequestParam("topic") String topic, @RequestBody WriteMessageRequest request) {
        producerService.writeMessage(topic, request);
    }

    @GetMapping
    public Optional<MessageDto> read(
            @RequestParam("topic") String topic,
            @RequestParam("groupId") String groupId,
            @RequestParam("clientId") int clientId) {
        return consumerService.readMessage(topic, groupId, clientId);
    }

    @PostMapping("/commit")
    public void commit(@RequestParam("id") String id, @RequestParam("groupId") String groupId) {
        consumerService.commitMessage(UUID.fromString(id), groupId);
    }
}
