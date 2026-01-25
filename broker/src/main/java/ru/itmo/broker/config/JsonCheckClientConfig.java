package ru.itmo.broker.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonCheckClientConfig {

    private static final PromptTemplate PROMPT_TEMPLATE = new PromptTemplate(
            """
                    Тебе даётся сообщение, которое надо проверить на то,
                    является ли оно корректным JSON-ом, в ответ ты должен выдать ОДНО СЛОВО:
                    'true' - если сообщение имеет корректную json структуру
                    'false' - в любом другом случае
                    Твой ответ должен корректно интерпретироваться java функцией Boolean.valueOf(твой ответ),
                    поэтому ничего лишнего быть не должно.
                    Ты не должен отвечать ничего другого, кроме этих двух слов.
                    """
    );

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultOptions(
                        OllamaChatOptions.builder().topP(0.7).topK(20).repeatPenalty(1.1).temperature(0.3).build()
                )
                .defaultSystem(PROMPT_TEMPLATE.render())
                .build();
    }
}
