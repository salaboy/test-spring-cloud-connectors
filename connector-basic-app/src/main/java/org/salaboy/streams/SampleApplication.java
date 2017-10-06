package org.salaboy.streams;

import java.util.HashMap;
import java.util.Map;

import org.salaboy.streams.model.IntegrationEvent;
import org.salaboy.streams.model.IntegrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBinding(MyChannels.class)
@RestController
public class SampleApplication implements CommandLineRunner {

    @Autowired
    private MessageChannel integrationResultsProducer;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class,
                              args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void run(String... strings) throws Exception {

        assert (integrationResultsProducer != null);
        assert (restTemplate != null);
    }

    @RequestMapping(value = "/payment/{processDefId}", method = RequestMethod.POST)
    public void consumePaymentIntegrationEventsRest(@RequestBody IntegrationEvent event,
                                                    @PathVariable("processDefId") String processDefinitionId) {
        System.out.println("Received Integration Event Via REST: " + event);
        consumePaymentIntegrationEvents(event,
                                        processDefinitionId);
    }

    @StreamListener(value = MyChannels.INTEGRATION_EVENTS_CONSUMER, condition = "headers['type']=='Payment'")
    public void consumePaymentIntegrationEvents(IntegrationEvent event,
                                                @Header("processDefinitionId") String processDefinitionId) {
        System.out.println("Received Integration Event Via MQ: " + event);
        System.out.println("Integration Event with processDefId in header:" + processDefinitionId);

        HttpEntity<Payment> payment = new HttpEntity<>(new Payment("GBP",
                                                                   123.4,
                                                                   "new minibrute",
                                                                   "ABC-123"));

        ResponseEntity<String> response = restTemplate
                .exchange("http://localhost:8081/payment",
                          HttpMethod.POST,
                          payment,
                          String.class);

        Map<String, Object> variables = new HashMap<>();
        variables.put("payment-status",
                      response.getBody());

        IntegrationResult integrationResult = new IntegrationResult(event.getCorrelationId(),
                                                                    variables);
        Message<IntegrationResult> message = MessageBuilder.withPayload(integrationResult).build();

        integrationResultsProducer.send(message);
    }

    class Payment {

        private String currency;
        private Double amount;
        private String reason;
        private String account;

        public Payment(String currency,
                       Double amount,
                       String reason,
                       String account) {
            this.currency = currency;
            this.amount = amount;
            this.reason = reason;
            this.account = account;
        }

        public Payment() {
        }

        public String getCurrency() {
            return currency;
        }

        public Double getAmount() {
            return amount;
        }

        public String getReason() {
            return reason;
        }

        public String getAccount() {
            return account;
        }
    }
}
