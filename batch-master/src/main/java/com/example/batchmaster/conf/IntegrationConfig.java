package com.example.batchmaster.conf;

import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
@EnableBatchIntegration
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
        return MessageChannels.direct().get();
    }

    @Bean
    public IntegrationFlow savePersonFlow(ConnectionFactory connectionFactory,
                                          MessageChannel savePersonChannel) {
        return IntegrationFlows
                .from(savePersonChannel)
                .handle(Jms.outboundAdapter(connectionFactory).destination(personSaveQueue))
                .get();
    }

    @Bean
    public QueueChannel savePersonReplyChannel() {
        return MessageChannels.queue().get();
    }

    @Bean
    public IntegrationFlow savePersonReplyFlow(ConnectionFactory connectionFactory,
                                               MessageChannel savePersonReplyChannel) {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory).destination(personSaveQueueReply))
                .channel(savePersonReplyChannel)
                .get();
    }
}
