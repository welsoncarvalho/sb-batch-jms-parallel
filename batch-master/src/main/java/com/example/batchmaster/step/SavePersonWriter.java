package com.example.batchmaster.step;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.integration.chunk.ChunkResponse;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.PollableChannel;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class SavePersonWriter extends ChunkMessageChannelItemWriter<ChunkResponse> {

    public SavePersonWriter(MessagingTemplate messagingTemplate,
                            PollableChannel pollableChannel) {

        super.setMessagingOperations(messagingTemplate);
        super.setReplyChannel(pollableChannel);
    }

}
