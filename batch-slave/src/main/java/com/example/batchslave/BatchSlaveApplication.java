package com.example.batchslave;

import com.example.batchslave.step.SavePersonProcessor;
import com.example.batchslave.step.SavePersonWriter;
import lombok.SneakyThrows;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.ServiceActivatorFactoryBean;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.handler.ReplyProducingMessageHandlerWrapper;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

import javax.jms.ConnectionFactory;

@SpringBootApplication
public class BatchSlaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchSlaveApplication.class, args);
	}

	@Bean
	public MessageChannel outPersonSaveQueue(ConnectionFactory connectionFactory) {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "outPersonSaveQueue")
	public MessageHandler outPersonSaveQueueAdapter(ConnectionFactory connectionFactory) {
		return Jms.outboundAdapter(connectionFactory)
				.destination("person_save_queue_reply")
				.get();
	}

	@Bean
	public PollableChannel inPersonSaveQueue(ConnectionFactory connectionFactory) {
		return Jms
				.pollableChannel(connectionFactory)
				.destination("person_save_queue")
				.get();
	}

	@Bean
	@ServiceActivator(
			inputChannel= "inPersonSaveQueue",
			outputChannel = "outPersonSaveQueue",
			poller = @Poller(fixedRate = "5"))
	public ChunkHandler<Object> stepSavePersonProcessor(
			SavePersonProcessor savePersonProcessor,
			SavePersonWriter savePersonWriter) {
		ChunkProcessorChunkHandler<Object> processor = new ChunkProcessorChunkHandler<>();
		processor.setChunkProcessor(new SimpleChunkProcessor<>(savePersonProcessor, savePersonWriter));
		return processor;
	}

}
