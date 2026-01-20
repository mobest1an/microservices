package ru.itmo.broker.service.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.broker.api.dto.responses.MessageDto;
import ru.itmo.broker.service.PartitionRouter;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final PartitionRouter partitionRouter;

    public MessageDto readMessage() {
        return null; //TODO
    }
}
