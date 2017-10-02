package com.salaboy.payments.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.salaboy.payments.model.Payment;
import com.salaboy.payments.model.PaymentStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimplePaymentController {

    private static Map<Payment, PaymentStatus> payments = new HashMap<>();

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public PaymentStatus processPayment(@RequestParam(value = "type", defaultValue = "simple") String type,
                                        @RequestBody Payment payment) {

        System.out.println("Payment Type: " + type);

        System.out.println("Payment: " + payment);
        PaymentStatus status = PaymentStatus.APPROVED;
        payments.put(payment,
                     status);
        return status;
    }

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public Collection<Payment> getPayments() {
        return payments.keySet();
    }

}
