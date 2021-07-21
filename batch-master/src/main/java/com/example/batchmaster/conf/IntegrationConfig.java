package com.example.batchmaster.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
public class IntegrationConfig {

    @Value("${person.save.queue}")
    private String personSaveQueue;

    @Value("${person.save.queue.reply}")
    private String personSaveQueueReply;

    @Bean
    public MessagingTemplate savePersonTemplate(MessageChannel savePersonChannel) {
        MessagingTemplate savePersonTemplate = new MessagingTemplate();
        savePersonTemplate.setDefaultChannel(savePersonChannel);
        savePersonTemplate.setReceiveTimeout(2000L);
        return savePersonTemplate;
    }

    @Bean
    public MessageChannel savePersonChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "savePersonChannel")
    public MessageHandler savePersonAdapter(ConnectionFactory connectionFactory) {
        return Jms.outboundAdapter(connectionFactory)
                .destination(personSaveQueue)
                .get();
    }

    @Bean
    public MessageChannel savePersonReplyChannel(ConnectionFactory connectionFactory) {
        return Jms.pollableChannel(connectionFactory)
                .destination(personSaveQueueReply)
                .get();
    }

//    @Bean
//    public JmsMessageDrivenEndpoint savePersonReplyAdapter(
//                    ConnectionFactory connectionFactory,
//                    MessageChannel savePersonReplyChannel) {
//        return Jms.messageDrivenChannelAdapter(connectionFactory)
//                .destination(personSaveQueueReply)
//                .outputChannel(savePersonReplyChannel)
//                .get();
//    }
}
