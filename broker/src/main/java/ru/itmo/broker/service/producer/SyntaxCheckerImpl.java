package ru.itmo.broker.service.producer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public class SyntaxCheckerImpl implements SyntaxChecker {

    private final ChatClient chatClient;

    @Override
    @Transactional
    public String check(String content) {
        var textResult = chatClient.prompt().user(content).call().content().trim();
        var boolResult = false;
        try {
            boolResult = Boolean.parseBoolean(textResult);
        } catch (Exception e) {
            log.info("illegal message for JSON-analys: {}", content);
        }
        return Boolean.toString(boolResult);
    }
}
