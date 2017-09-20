/*
 * Copyright 2017 Alfresco and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.salaboy.streams;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.salaboy.streams.model.IntegrationEvent;
import org.salaboy.streams.model.IntegrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.test.junit.rabbit.RabbitTestSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test-application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EnableBinding(ClientStreams.class)
public class MyAppStreamsTest {

    private static final String relativeResultsEndpoint = "/api/results";
    private static final String callEndpoint = "/api/call";
    private static final String call2Endpoint = "/api/call2";

    @ClassRule
    public static RabbitTestSupport rabbitTestSupport = new RabbitTestSupport();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageChannel integrationEventsProducer;

    public static boolean integrationResultArrived = false;

    @EnableAutoConfiguration
    public static class StreamHandler {


        @StreamListener(value = ClientStreams.INTEGRATION_RESULT_CONSUMER)
        public void consumeIntegrationResults(IntegrationResult integrationResult) throws InterruptedException {
            System.out.println(">>> integrationResult: " + integrationResult);
//
            integrationResultArrived = true;
        }
    }

    @Test
    public void getAllMessagesTests() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("var1",
                      "value1");
        variables.put("var2",
                      new Long(1));
        String correlationId = UUID.randomUUID().toString();
        IntegrationEvent integrationEvent = new IntegrationEvent(correlationId,
                                                                 variables);
        Message<IntegrationEvent> message = MessageBuilder.withPayload(integrationEvent).build();
        integrationEventsProducer.send(message);

        while(!integrationResultArrived){
            System.out.println("Waiting for result to arrive ...");
            Thread.sleep(100);
        }

        assertThat(integrationResultArrived).isTrue();
    }
}
