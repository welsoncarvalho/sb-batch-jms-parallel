package com.example.batchslave.conf;

import com.example.batchslave.step.SavePersonProcessor;
import com.example.batchslave.step.SavePersonWriter;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
public class IntegrationConfig {

    @Value("${person.save.queue}")
    private String personSaveQueue;
    
    @Value("${person.save.queue.reply}")
    private String personSaveQueueReply;

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
    @ServiceActivator(
            inputChannel= "inPersonSaveQueue",
            outputChannel = "outPersonSaveQueue")
    public ChunkHandler<String> stepSavePersonProcessor(
            SavePersonProcessor savePersonProcessor,
            SavePersonWriter savePersonWriter) {
        ChunkProcessorChunkHandler<String> processor = new ChunkProcessorChunkHandler<>();
        processor.setChunkProcessor(new SimpleChunkProcessor<>(savePersonProcessor, savePersonWriter));
        return processor;
    }
}
