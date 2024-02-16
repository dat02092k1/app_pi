package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailsDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailsController {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailsDTO orderDetailsDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMsg = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMsg.toString());
        }

        return ResponseEntity.ok("Order details created " + orderDetailsDTO.getOrderId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderDetails(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok("Order details " + id);
    }

    // get list order details of an order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok("Order details by order id " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailsDTO orderDetailsDTO
    ) {
        return ResponseEntity.ok("updateOrderDetails with id " + id + " " + orderDetailsDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetails(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }



}
