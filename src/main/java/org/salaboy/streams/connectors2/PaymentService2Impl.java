package org.salaboy.streams.connectors2;

import org.salaboy.streams.PaymentService;

public class PaymentService2Impl implements PaymentService {

    @Override
    public String doPayment(String money) {
        System.out.println("Processing Payment of: " + money + " ...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Payment OK";
    }
}
