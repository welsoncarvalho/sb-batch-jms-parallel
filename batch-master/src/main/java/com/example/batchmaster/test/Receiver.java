package com.example.batchmaster.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class Receiver {

    @JmsListener(destination = "person_save_queue")
    public void processMessage(ChunkRequest<Object> request) {
        log.info("message: " + request.toString());
    }
}
