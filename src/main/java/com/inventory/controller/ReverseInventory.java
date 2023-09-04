package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.Inventory;
import com.inventory.model.InventoryEvent;
import com.inventory.model.PaymentEvent;
import com.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class ReverseInventory {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @KafkaListener(topics = "reversed-inventory", groupId = "inventory-group")
    public void reverseInventory(String event) {

        try {

            InventoryEvent inventoryEvent = new ObjectMapper().readValue(event, InventoryEvent.class);

            Iterable<Inventory> inv = this.repository.findByItem(inventoryEvent.getOrder().getItem());

            inv.forEach(i -> {

                i.setQuantity(i.getQuantity() + inventoryEvent.getOrder().getQuantity());

                this.repository.save(i);
            });
            // reverse previous task
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setOrder(inventoryEvent.getOrder());
            paymentEvent.setType("PAYMENT_REVERSED");
            this.kafkaTemplate.send("reversed-payments", paymentEvent);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
