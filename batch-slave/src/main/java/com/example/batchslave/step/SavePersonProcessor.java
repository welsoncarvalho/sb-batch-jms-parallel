package com.example.batchslave.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SavePersonProcessor implements ItemProcessor<Object, Object> {

    @Override
    public Object process(Object o) throws Exception {
        log.info("processing ... " + o);
        return o;
    }
}
