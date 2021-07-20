package com.example.batchmaster.test;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class Publisher {

    private final JmsTemplate jmsTemplate;

    public void publish(String message) {
        log.info("publishing ... " + message);
        jmsTemplate.convertAndSend("person_save_queue", message);
    }
}
