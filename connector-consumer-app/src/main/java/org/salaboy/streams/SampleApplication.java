package org.salaboy.streams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.salaboy.service.api.PaymentService;
import org.salaboy.streams.model.IntegrationEvent;
import org.salaboy.streams.model.IntegrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(MyChannels.class)
@RestController
public class SampleApplication implements CommandLineRunner {

    @Autowired
    private MessageChannel integrationResultsProducer;

    private Cloud cloud = new CloudFactory().getCloud();

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class,
                              args);
    }

    @Override
    public void run(String... strings) throws Exception {

        assert (integrationResultsProducer != null);
        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
        System.out.println("Service Infos size = " + serviceInfos.size());
        for (ServiceInfo s : serviceInfos) {
            System.out.println(">>> Service Info: " + s.getId());
        }
    }

    @RequestMapping(value = "/payment/{processDefId}", method = RequestMethod.POST)
    public void consumePaymentIntegrationEventsRest(@RequestBody IntegrationEvent event, @PathVariable("processDefId") String processDefinitionId) {
        System.out.println("Received Integration Event Via REST: " + event);
        consumePaymentIntegrationEvents(event, processDefinitionId);
    }

    @StreamListener(value = MyChannels.INTEGRATION_EVENTS_CONSUMER, condition = "headers['type']=='Payment'")
    public void consumePaymentIntegrationEvents(IntegrationEvent event,
                                                @Header("processDefinitionId") String processDefinitionId) {
        System.out.println("Received Integration Event Via MQ: " + event);
        System.out.println("Integration Event with processDefId in header:" + processDefinitionId);

        PaymentService payment = cloud.getServiceConnector("payment",
                                                           PaymentService.class,
                                                           null);

        if (payment == null) {
            throw new IllegalStateException("finding a payment service failed !");
        }

        String result = payment.doPayment("GBP",
                                          123.4,
                                          "new minibrute",
                                          "ABC-123");

        Map<String, Object> variables = new HashMap<>();
        variables.put("payment-status",
                      result);

        IntegrationResult integrationResult = new IntegrationResult(event.getCorrelationId(),
                                                                    variables);
        Message<IntegrationResult> message = MessageBuilder.withPayload(integrationResult).build();

        integrationResultsProducer.send(message);
    }
}
