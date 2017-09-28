package org.salaboy.streams;

import org.springframework.cloud.service.UriBasedServiceInfo;

public interface PaymentService {

    String doPayment(String currency,
                     Double amount,
                     String reason,
                     String account);

}
