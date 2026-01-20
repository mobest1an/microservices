package ru.itmo.broker.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @Column(name = "cliend_id")
    private Map<Integer, Integer> clients;
}
