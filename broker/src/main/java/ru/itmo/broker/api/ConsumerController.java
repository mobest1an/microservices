package ru.itmo.broker.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.broker.api.dto.responses.ConsumerGroupDto;
import ru.itmo.broker.service.ConsumerGroupService;

/**
 * @author erik.karapetyan
 */
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class ConsumerController {

    private final ConsumerGroupService consumerGroupService;

    @PostMapping("/{groupId}/join")
    public ConsumerGroupDto join(@PathVariable String groupId, @RequestParam("topic") String topic) {
        return consumerGroupService.join(groupId, topic);
    }

    @PostMapping("/{groupId}/leave")
    public ConsumerGroupDto leave(@PathVariable String groupId, @RequestParam("topic") String topic, int clientId) {
        return consumerGroupService.leave(groupId, topic, clientId);
    }
}
