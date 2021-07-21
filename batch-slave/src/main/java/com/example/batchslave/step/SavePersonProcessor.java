package com.example.batchslave.step;

import com.example.batchslave.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class SavePersonProcessor implements ItemProcessor<String, Person> {

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("[dd/MM/yyyy][dd/M/yyyy][d/M/yyyy]");

    @Override
    public Person process(String item) throws Exception {

        String[] record = item.split(",");

        return Person.builder()
                .id(UUID.randomUUID().toString())
                .firstName(record[0])
                .lastName(record[1])
                .email(record[2])
                .birthDate(LocalDate.parse(record[3], formatter))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
