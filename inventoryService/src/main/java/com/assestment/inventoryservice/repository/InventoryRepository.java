package com.assestment.inventoryservice.repository;

import org.springframework.stereotype.Repository;
import com.assestment.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(Long productId);
}
