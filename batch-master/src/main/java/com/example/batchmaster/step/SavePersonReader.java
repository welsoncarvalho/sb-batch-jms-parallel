package com.example.batchmaster.step;

import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@StepScope
@Component
public class SavePersonReader implements ItemReader<String> {

    private Iterator<String> persons;

    @BeforeStep
    public void before() {
        this.persons = List.of(
                "Person One", "Person Two"
        ).iterator();
    }

    @Override
    public String read() throws Exception {
        if (persons.hasNext())
            return persons.next();

        return null;
    }
}
