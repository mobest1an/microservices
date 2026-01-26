package ru.itmo.demo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import requests.WriteMessageRequest;
import ru.itmo.demo.DemoProducer;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    private final DemoProducer demoProducer;

    public void someBl(WriteMessageRequest request, boolean synMode) {
        long current = System.currentTimeMillis();
        demoProducer.produce(request.content(), synMode);
        logger.info("Processing took: {} ms", System.currentTimeMillis() - current);
    }
}
