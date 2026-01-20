package ru.itmo.broker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.broker.dao.ConsumerGroupDao;
import ru.itmo.broker.model.ConsumerGroup;
import ru.itmo.broker.model.Topic;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class PartitionRouter {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int joinConsumerGroup(ConsumerGroup consumerGroup) {
        Map<Integer, Integer> partitionDistribution = consumerGroup.getClients();

        int clientId = generateClientId(partitionDistribution);
        Map<Integer, Integer> rebalancedPartitionDistribution = rebalance(partitionDistribution, clientId);

        consumerGroup.setClients(rebalancedPartitionDistribution);
        return clientId;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void leaveConsumerGroup(ConsumerGroup consumerGroup, int clientId) {
        Map<Integer, Integer> partitionDistribution = consumerGroup.getClients();

        boolean clientExists = partitionDistribution.containsValue(clientId);
        if (!clientExists) {
            throw new IllegalArgumentException(
                    "Client id " + clientId + " is not a member of group [" + consumerGroup.getGroupId() + "/" + consumerGroup.getTopic().getName() + "]");
        }

        Map<Integer, Integer> cleared = new HashMap<>(partitionDistribution);
        cleared.replaceAll((k, v) -> (clientId == v) ? ConsumerGroupDao.FREE_PARTITION_DEFAULT_CLIENT_ID : v);
        Map<Integer, Integer> rebalancedPartitionDistribution = rebalanceExisting(cleared);

        consumerGroup.setClients(rebalancedPartitionDistribution);
    }

    public int getPartitionForProducer(Topic topic) {
        List<Integer> partitions = IntStream.rangeClosed(1, topic.getPartitionCount())
                .boxed()
                .toList();

        return partitions.get(ThreadLocalRandom.current().nextInt(partitions.size()));
    }

    public int getPartitionForClient(ConsumerGroup consumerGroup, int clientId) {
        Map<Integer, Integer> partitionDistribution = consumerGroup.getClients();

         List<Integer> partitions = partitionDistribution.entrySet().stream()
                .filter(it -> Integer.valueOf(clientId).equals(it.getValue()))
                .map(Map.Entry::getKey)
                .toList();

         return partitions.get(ThreadLocalRandom.current().nextInt(partitions.size()));
    }

    private int generateClientId(Map<Integer, Integer> partitionDistribution) {
        Set<Integer> used = partitionDistribution.values().stream()
                .filter(it -> !it.equals(ConsumerGroupDao.FREE_PARTITION_DEFAULT_CLIENT_ID)).collect(Collectors.toSet());

        int maxIdExclusive = Math.max(used.size() + 2, 100);

        List<Integer> free = IntStream.rangeClosed(1, maxIdExclusive)
                .filter(i -> !used.contains(i))
                .boxed()
                .toList();

        if (free.isEmpty()) {
            throw new RuntimeException("No free partitions left");
        }

        return free.get(ThreadLocalRandom.current().nextInt(free.size()));
    }

    private Map<Integer, Integer> rebalance(Map<Integer, Integer> currentPartitionDistribution, int clientId) {
        List<Integer> partitions = new ArrayList<>(currentPartitionDistribution.keySet());
        List<Integer> clients = getUniqueClients(currentPartitionDistribution, clientId);

        int partitionCount = currentPartitionDistribution.size();
        int clientsCount = clients.size();

        if (clientsCount > partitionCount) {
            throw new IllegalStateException("Clients count cannot be more than partition count");
        }

        int partitionPerEachClient = partitionCount / clientsCount;
        int remainingPartitionsCount = partitionCount % clientsCount;

        Map<Integer, Integer> rebalancedPartitionDistribution = new HashMap<>(partitionCount);
        int partitionIdx = 0;
        for (int i = 0; i < clientsCount; i++) {
            int client = clients.get(i);
            int partitionsForThisClient = partitionPerEachClient + (i < remainingPartitionsCount ? 1 : 0);

            for (int j = 0; j < partitionsForThisClient; j++) {
                rebalancedPartitionDistribution.put(partitions.get(partitionIdx++), client);
            }
        }

        return rebalancedPartitionDistribution;
    }

    private Map<Integer, Integer> rebalanceExisting(Map<Integer, Integer> partitionDistribution) {
        List<Integer> clients = partitionDistribution.values().stream()
                .filter(it -> !it.equals(ConsumerGroupDao.FREE_PARTITION_DEFAULT_CLIENT_ID))
                .distinct()
                .toList();

        if (clients.isEmpty()) {
            return partitionDistribution;
        }

        return rebalance(partitionDistribution, clients.getFirst());
    }

    private List<Integer> getUniqueClients(Map<Integer, Integer> partitionDistribution, int clientId) {
        Set<Integer> clients = partitionDistribution.values().stream()
                .filter(it -> !it.equals(ConsumerGroupDao.FREE_PARTITION_DEFAULT_CLIENT_ID))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        clients.add(clientId);
        return new ArrayList<>(clients);
    }
}
