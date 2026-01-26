package ru.itmo.consumer;

/**
 * @author erik.karapetyan
 */
public abstract class AbstractProcessorFactory {

    public abstract QueueProcessorBase create();
}
