package ru.itmo.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.demo.DemoProducer;

/**
 * @author erik.karapetyan
 */
@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoProducer demoProducer;

    public void someBl(String content) {
        demoProducer.produce(content);
    }
}
