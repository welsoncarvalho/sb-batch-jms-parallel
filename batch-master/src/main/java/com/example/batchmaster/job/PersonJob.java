package com.example.batchmaster.job;

import com.example.batchmaster.step.SavePersonReader;
import com.example.batchmaster.step.SavePersonWriter;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkResponse;
import org.springframework.batch.integration.chunk.RemoteChunkHandlerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class PersonJob {

    private final ApplicationContext applicationContext;

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
            SavePersonReader savePersonReader,
            SavePersonWriter savePersonWriter) {

        return stepBuilderFactory
                .get("step_save_person")
                .<String, ChunkResponse>chunk(100)
                .reader(savePersonReader)
                .writer(savePersonWriter)
                .build();
    }

    @Bean
    @StepScope
    public ChunkHandler<ChunkResponse> stepSavePersonHandler(
            TaskletStep stepSavePerson,
            SavePersonWriter savePersonWriter) throws Exception {

        RemoteChunkHandlerFactoryBean<ChunkResponse> factoryBean = new RemoteChunkHandlerFactoryBean<>();
        factoryBean.setStep(stepSavePerson);
        factoryBean.setChunkWriter(savePersonWriter);
        return factoryBean.getObject();
    }

}
