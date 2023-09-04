package com.inventory.model;

import lombok.Data;

@Data
public class PaymentEvent {

    private String type;

    private CustomerOrder order;
}
