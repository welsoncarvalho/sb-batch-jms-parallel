package com.example.batchmaster.job;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.integration.chunk.ChunkResponse;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;

@Configuration
@AllArgsConstructor
public class PersonJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(Step stepSavePerson) {
        return jobBuilderFactory
                .get("person_job")
                .incrementer(new RunIdIncrementer())
                .start(stepSavePerson)
                .build();
    }

    @Bean
    public TaskletStep stepSavePerson(
            ItemReader<String> savePersonReader,
            ItemWriter<ChunkResponse> savePersonWriter) {

        return stepBuilderFactory
                .get("step_save_person")
                .<String, ChunkResponse>chunk(100)
                .reader(savePersonReader)
                .writer(savePersonWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<String> savePersonReader() throws Exception {
        return new FlatFileItemReaderBuilder<String>()
                .name("savePersonReader")
                .resource(new ClassPathResource("csv/person.csv"))
                .lineTokenizer(new DelimitedLineTokenizer("\n"))
                .fieldSetMapper(fieldSet -> fieldSet.readRawString(0)) // I want to get the line
                .build();
    }

    @Bean
    @StepScope
    public ChunkMessageChannelItemWriter<ChunkResponse> savePersonWriter(
            MessagingTemplate savePersonTemplate,
            QueueChannel savePersonReplyChannel) {

        ChunkMessageChannelItemWriter<ChunkResponse> writer = new ChunkMessageChannelItemWriter<>();
        writer.setMessagingOperations(savePersonTemplate);
        writer.setReplyChannel(savePersonReplyChannel);

        return writer;
    }
}
