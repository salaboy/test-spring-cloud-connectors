package org.salaboy.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyChannels {

    String INTEGRATION_EVENTS_CONSUMER = "integrationEventsConsumer";

    @Input(INTEGRATION_EVENTS_CONSUMER)
    SubscribableChannel integrationEventsConsumer();

    String INTEGRATION_RESULT_PRODUCER = "integrationResultsProducer";

    @Output(INTEGRATION_RESULT_PRODUCER)
    MessageChannel integrationResultsProducer();

}
