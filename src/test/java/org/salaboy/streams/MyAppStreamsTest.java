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
import org.salaboy.streams.model.AsyncContext;
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

import static org.assertj.core.api.Assertions.*;

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

    private static Map<String, AsyncContext> correlation = new HashMap<>();
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageChannel integrationEventsProducer;

    public static boolean integrationResultArrived = false;

    @EnableAutoConfiguration
    public static class StreamHandler {

        @StreamListener(value = ClientStreams.INTEGRATION_RESULT_CONSUMER)
        public void consumeIntegrationResults(IntegrationResult integrationResult) throws InterruptedException {
            System.out.println(">>> Result Recieved Back from Connector: " + integrationResult);
            String correlationId = integrationResult.getCorrelationId();
            AsyncContext context = correlation.get(correlationId);
            assertThat(integrationResult.getVariables().get("payment-status")).isEqualTo("\"APPROVED\"");
            System.out.println(">> Continue with processDefinition: " + context.getProcessInstanceId());
            System.out.println(">> \t\t Task: " + context.getProcessInstanceId());

            integrationResultArrived = true;
        }
    }

    @Test
    public void sendIntegrationEventTest() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("var1",
                      "value1");
        variables.put("var2",
                      new Long(1));
        String correlationId = "correlationId-" + UUID.randomUUID().toString();
        String processInstanceId = "processInstanceId-" + UUID.randomUUID().toString();
        String processDefinitionId = "processDefinitionId-" + UUID.randomUUID().toString();
        String taskId = "taskId-" + UUID.randomUUID().toString();
        String executionId = "executionId-" + UUID.randomUUID().toString();

        correlation.put(correlationId,
                        new AsyncContext(processInstanceId,
                                         taskId,
                                         executionId));

        IntegrationEvent integrationEvent = new IntegrationEvent(correlationId,
                                                                 processInstanceId,
                                                                 taskId,
                                                                 executionId,
                                                                 variables);

        // We will send the Integration Event with data inside, and the HEADERs will contain data that we might want to use to filter.

        Message<IntegrationEvent> message = MessageBuilder.withPayload(integrationEvent)
                .setHeader("type",
                           "Payment")
                .setHeader("processDefinitionId", // this is option and only if we are interested in fitering by processDefinitionId
                           processDefinitionId)
                .build();

        integrationEventsProducer.send(message);

        while (!integrationResultArrived) {
            System.out.println("Waiting for result to arrive ...");
            Thread.sleep(100);
        }

        assertThat(integrationResultArrived).isTrue();
    }
}
