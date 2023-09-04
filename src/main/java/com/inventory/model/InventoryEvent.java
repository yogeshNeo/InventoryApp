package com.inventory.model;

import lombok.Data;

@Data
public class InventoryEvent {

    private String type;

    private CustomerOrder order;
}
