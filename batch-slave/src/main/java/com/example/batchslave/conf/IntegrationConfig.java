package com.example.batchslave.conf;

import com.example.batchslave.step.SavePersonProcessor;
import com.example.batchslave.step.SavePersonWriter;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

import javax.jms.ConnectionFactory;

@Configuration
public class IntegrationConfig {

    @Value("${person.save.queue}")
    private String personSaveQueue;
    
    @Value("${person.save.queue.reply}")
    private String personSaveQueueReply;

    @Bean
    public MessageChannel outPersonSaveQueue(ConnectionFactory connectionFactory) {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "outPersonSaveQueue")
    public MessageHandler outPersonSaveQueueAdapter(ConnectionFactory connectionFactory) {
        return Jms.outboundAdapter(connectionFactory)
                .destination(personSaveQueueReply)
                .get();
    }

    @Bean
    public PollableChannel inPersonSaveQueue(ConnectionFactory connectionFactory) {
        return Jms
                .pollableChannel(connectionFactory)
                .destination(personSaveQueue)
                .get();
    }

    @Bean
    @ServiceActivator(
            inputChannel= "inPersonSaveQueue",
            outputChannel = "outPersonSaveQueue",
            poller = @Poller(fixedRate = "5"))
    public ChunkHandler<String> stepSavePersonProcessor(
            SavePersonProcessor savePersonProcessor,
            SavePersonWriter savePersonWriter) {
        ChunkProcessorChunkHandler<String> processor = new ChunkProcessorChunkHandler<>();
        processor.setChunkProcessor(new SimpleChunkProcessor<>(savePersonProcessor, savePersonWriter));
        return processor;
    }
}
