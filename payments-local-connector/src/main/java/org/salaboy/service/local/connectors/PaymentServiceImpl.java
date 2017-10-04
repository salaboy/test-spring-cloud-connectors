package org.salaboy.service.local.connectors;

import org.salaboy.service.api.PaymentService;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PaymentServiceImpl implements PaymentService {

    private RestTemplate restTemplate;
    private UriBasedServiceInfo serviceInfo;

    public PaymentServiceImpl(RestTemplate restTemplate,
                              UriBasedServiceInfo serviceInfo) {
        this.restTemplate = restTemplate;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public String doPayment(String currency,
                            Double amount,
                            String reason,
                            String account) {
        HttpEntity<Payment> payment = new HttpEntity<>(new Payment(currency,
                                                                   amount,
                                                                   reason,
                                                                   account));
        ResponseEntity<String> response = restTemplate
                .exchange("http://" + serviceInfo.getHost() + ":" + serviceInfo.getPort() + "/" + serviceInfo.getPath(),
                          HttpMethod.POST,
                          payment,
                          String.class);

        return response.getBody();
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
