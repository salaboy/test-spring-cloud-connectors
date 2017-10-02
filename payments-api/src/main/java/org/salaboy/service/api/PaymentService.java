package org.salaboy.service.api;

public interface PaymentService {

    String doPayment(String currency,
                     Double amount,
                     String reason,
                     String account);
}
