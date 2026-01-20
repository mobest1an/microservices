package ru.itmo.broker.model;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Topic {

    @Id
    private String name;
    private Integer partitionCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "offsets",
            joinColumns = @JoinColumn(name = "topic_name")
    )
    @MapKeyColumn(name = "partition")
    @Column(name = "partition_offset", nullable = false)
    private Map<Integer, Long> offsets;
}
