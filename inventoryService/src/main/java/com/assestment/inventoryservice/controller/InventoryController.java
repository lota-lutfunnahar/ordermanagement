package com.assestment.inventoryservice.controller;

import com.assestment.inventoryservice.entity.Inventory;
import com.assestment.inventoryservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory>  getInventory(@PathVariable Long productId) {

        log.info("Received request of product id {}", productId);
        Inventory responseData = inventoryService.getInventoryByProductId(productId);
        if (responseData == null || ObjectUtils.isEmpty(responseData)) {
            log.warn("Not found specific product {} ", responseData);
            return ResponseEntity.noContent().build();
        }
        log.info("Received response of product id {}", responseData);
        return ResponseEntity.ok(responseData);
    }
}
