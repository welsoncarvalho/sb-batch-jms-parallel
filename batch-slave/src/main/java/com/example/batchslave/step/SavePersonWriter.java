package com.example.batchslave.step;

import com.example.batchslave.model.Person;
import com.example.batchslave.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class SavePersonWriter implements ItemWriter<Person> {

    private final PersonRepository personRepository;

    @Override
    public void write(List<? extends Person> list) throws Exception {
        personRepository.saveAll(list);
    }
}
