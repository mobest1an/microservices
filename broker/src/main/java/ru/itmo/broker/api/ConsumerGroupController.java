package ru.itmo.broker.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import responses.ConsumerGroupDto;
import ru.itmo.broker.service.consumer.ConsumerGroupService;

/**
 * @author erik.karapetyan
 */
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class ConsumerGroupController {

    private final ConsumerGroupService consumerGroupService;

    @PostMapping("/{groupId}")
    public ConsumerGroupDto create(@PathVariable String groupId, @RequestParam("topic") String topic) {
        return consumerGroupService.create(groupId, topic);
    }

    @PostMapping("/{groupId}/join")
    public ConsumerGroupDto join(@PathVariable String groupId, @RequestParam("topic") String topic) {
        return consumerGroupService.join(groupId, topic);
    }

    @PostMapping("/{groupId}/leave")
    public ConsumerGroupDto leave(@PathVariable String groupId, @RequestParam("topic") String topic, int clientId) {
        return consumerGroupService.leave(groupId, topic, clientId);
    }
}
