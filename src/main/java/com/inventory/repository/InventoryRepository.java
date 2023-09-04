package com.inventory.repository;

import com.inventory.model.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory,Long> {

    Iterable<Inventory> findByItem(String item);
}
