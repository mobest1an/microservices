package ru.itmo.broker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
}
