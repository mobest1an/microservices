package ru.itmo.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author erik.karapetyan
 */
public class ConsumerManager implements SmartLifecycle {

    private final AbstractProcessorFactory factory;
    private final ConsumerProperties consumerProps;
    private final ThreadPoolTaskExecutor taskExecutor;

    private final List<Future<?>> futures = new ArrayList<>();

    private volatile boolean running = false;

    public ConsumerManager(
            AbstractProcessorFactory factory,
            ConsumerProperties consumerProps,
            ThreadPoolTaskExecutor taskExecutor
    ) {
        this.factory = factory;
        this.consumerProps = consumerProps;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void start() {
        int partitions = resolvePartitions();

        for (int p = 0; p < partitions; p++) {
            QueueProcessorBase worker = factory.create();
            Future<?> f = taskExecutor.submit(worker);
            futures.add(f);
        }

        running = true;
    }

    @Override
    public void stop() {
        futures.forEach(f -> f.cancel(true));

        taskExecutor.shutdown();
        try {
            if (!taskExecutor.getThreadPoolExecutor()
                    .awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
                taskExecutor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private int resolvePartitions() {
        return consumerProps.getPartitions();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
