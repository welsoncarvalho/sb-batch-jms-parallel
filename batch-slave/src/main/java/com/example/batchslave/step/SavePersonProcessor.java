package com.example.batchslave.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SavePersonProcessor implements ItemProcessor<Object, Object> {

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("[dd/MM/yyyy][dd/M/yyyy][d/M/yyyy]");

    @Override
    public Object process(Object o) throws Exception {
        log.info("processing ... " + o);
        return o;
    }
}
