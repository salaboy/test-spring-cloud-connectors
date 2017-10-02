package com.salaboy.payments.model;

import java.util.Date;

public class Payment {

    private String currency;
    private Double amount;
    private String reason;
    private String account;
    private Date timestamp;

    public Payment() {
    }

    public Payment(String currency,
                   Double amount,
                   String reason,
                   String account) {
        this.timestamp = new Date();
        this.currency = currency;
        this.amount = amount;
        this.reason = reason;
        this.account = account;
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

    public Date getTimestamp() {
        return timestamp;
    }
}
