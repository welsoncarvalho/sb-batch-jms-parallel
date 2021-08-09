package com.example.batchslave.conf;

import com.example.batchslave.model.Person;
import com.example.batchslave.step.SavePersonProcessor;
import com.example.batchslave.step.SavePersonWriter;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
@EnableBatchIntegration
public class SlaveConfig {

    @Value("${person.save.queue}")
    private String personSaveQueue;
    
    @Value("${person.save.queue.reply}")
    private String personSaveQueueReply;

    @Autowired
    private RemoteChunkingWorkerBuilder<String, Person> remoteChunkingWorkerBuilder;

    @Bean
    public MessageChannel outPersonSaveQueue(ConnectionFactory connectionFactory) {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow outboundFlow(ConnectionFactory connectionFactory, MessageChannel outPersonSaveQueue) {
        return IntegrationFlows
                .from(outPersonSaveQueue)
                .handle(Jms.outboundAdapter(connectionFactory).destination(personSaveQueueReply))
                .get();
    }

    @Bean
    public MessageChannel inPersonSaveQueue(ConnectionFactory connectionFactory) {
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConnectionFactory connectionFactory, MessageChannel inPersonSaveQueue) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination(personSaveQueue))
                .channel(inPersonSaveQueue)
                .get();
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow(SavePersonProcessor savePersonProcessor,
                                                 SavePersonWriter savePersonWriter,
                                                 MessageChannel inPersonSaveQueue,
                                                 MessageChannel outPersonSaveQueue) {
        return this.remoteChunkingWorkerBuilder
                .itemProcessor(savePersonProcessor)
                .itemWriter(savePersonWriter)
                .inputChannel(inPersonSaveQueue)
                .outputChannel(outPersonSaveQueue)
                .build();
    }
}
