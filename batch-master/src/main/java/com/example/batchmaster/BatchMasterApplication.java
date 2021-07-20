package com.example.batchmaster;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

import javax.jms.ConnectionFactory;

@EnableBatchProcessing
@SpringBootApplication
public class BatchMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchMasterApplication.class, args);
	}

	@Bean
	public MessagingTemplate messagingTemplate(MessageChannel messageChannel) {
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setDefaultChannel(messageChannel);
		messagingTemplate.setReceiveTimeout(2000L);
		return messagingTemplate;
	}

	@Bean
	public MessageChannel messageChannel(ConnectionFactory connectionFactory) {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "messageChannel")
	public MessageHandler messageChannelAdapter(ConnectionFactory connectionFactory) {
		return Jms.outboundAdapter(connectionFactory)
				.destination("person_save_queue")
				.get();
	}

	@Bean
	public MessageChannel pollableChannel(ConnectionFactory connectionFactory) {
		return Jms
				.pollableChannel(connectionFactory)
				.destination("person_save_queue_reply")
				.get();
	}

	@Bean
	public JmsMessageDrivenEndpoint messageDrivenEndpoint(ConnectionFactory connectionFactory) {
		return Jms.messageDrivenChannelAdapter(connectionFactory)
				.destination("person_save_queue_reply")
				.outputChannel("pollableChannel")
				.get();
	}
}
