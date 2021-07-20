package com.example.batchslave.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SavePersonWriter implements ItemWriter<Object> {

    @Override
    public void write(List<?> list) throws Exception {
        list.forEach(i -> log.info("writing ... " + i));
    }
}
