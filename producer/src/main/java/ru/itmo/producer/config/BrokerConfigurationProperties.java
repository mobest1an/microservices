package ru.itmo.producer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author erik.karapetyan
 */
@ConfigurationProperties(prefix = "broker")
@Getter
@Setter
public class BrokerConfigurationProperties {

    private String messageBaseUrl;
}
