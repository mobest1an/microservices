package ru.itmo.broker.service.producer;

/**
 * @author erik.karapetyan
 */
public interface SyntaxChecker {

    String enrich(String content);
}
