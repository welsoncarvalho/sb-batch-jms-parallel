package com.example.batchmaster.controller;

import com.example.batchmaster.test.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final Publisher publisher;

    @GetMapping("/message/{message}")
    public String send(@PathVariable("message") String message) {
        publisher.publish(message);
        return message;
    }
}
