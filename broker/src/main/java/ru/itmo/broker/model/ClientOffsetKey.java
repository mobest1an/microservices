package ru.itmo.broker.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author erik.karapetyan
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientOffsetKey {

    private Integer clientId;
//    private String groupId;
    private Integer partition;
}
