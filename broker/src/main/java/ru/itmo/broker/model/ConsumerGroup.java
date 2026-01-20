package ru.itmo.broker.model;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyClass;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author erik.karapetyan
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsumerGroup {

    @Id
    private String groupId;

    @ManyToOne
    @JoinColumn(name = "topic_name", nullable = false)
    private Topic topic;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "clients",
            joinColumns = @JoinColumn(name = "group_id")
    )
    @MapKeyColumn(name = "partition")
    @Column(name = "cliend_id", nullable = false)
    private Map<Integer, Integer> clients;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "client_offsets",
            joinColumns = @JoinColumn(name = "group_id")
    )
    @MapKeyClass(ClientOffsetKey.class)
    @Column(name = "client_offset")
    private Map<ClientOffsetKey, Long> clientOffsets;
}
