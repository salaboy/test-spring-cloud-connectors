package org.salaboy.streams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
@EnableBinding(MyChannels.class)
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

    @StreamListener(value = MyChannels.INTEGRATION_EVENTS_CONSUMER)
    public void consumeIntegrationEvents(IntegrationEvent event) {
        System.out.println("Recieved Integration Event: " + event);
        PaymentService payment = cloud.getServiceConnector("payment",
                                                           PaymentService.class,
                                                           null);

        if (payment == null) {
            throw new IllegalStateException("null rest template to connect to payment service!");
        }
        System.out.println(">>> Rest Template OK!!!");

        String s = payment.doPayment("1000");

        Map<String, Object> variables = new HashMap<>();
        variables.put("payment-result",
                      s);

        IntegrationResult integrationResult = new IntegrationResult(event.getCorrelationId(),
                                                                    variables);
        Message<IntegrationResult> message = MessageBuilder.withPayload(integrationResult).build();

        integrationResultsProducer.send(message);
    }
}
