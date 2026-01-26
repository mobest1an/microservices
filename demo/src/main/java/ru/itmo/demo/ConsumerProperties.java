package ru.itmo.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author erik.karapetyan
 */
@Getter
@Setter
@AllArgsConstructor
public class ConsumerProperties {

    private final String topic;
    private final String groupId;
    private final int partitions;
}
