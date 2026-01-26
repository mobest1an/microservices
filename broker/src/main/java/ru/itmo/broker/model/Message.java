package ru.itmo.broker.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import responses.MessageDto;

/**
 * @author erik.karapetyan
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

    @Id
    private UUID id;
    private String content;
    private String header;
    private int partition;
    private long msgOffset;
    private boolean commited;

    @ManyToOne
    @JoinColumn(name = "topic_name", nullable = false)
    private Topic topic;

    public MessageDto fromModel() {
        return new MessageDto(
                this.getId(),
                this.getContent(),
                this.getHeader(),
                this.getPartition(),
                this.getMsgOffset(),
                this.isCommited(),
                this.getTopic().getName()
        );
    }
}
