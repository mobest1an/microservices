package ru.itmo.broker.service.producer;

import org.springframework.stereotype.Component;

/**
 * @author erik.karapetyan
 */
@Component
public class StubSyntaxChecker implements SyntaxChecker {

    @Override
    public String check(String content) {
        return content;
    }
}
