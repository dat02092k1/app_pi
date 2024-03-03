package com.project.shopapp.controllers;

import com.project.shopapp.dtos.order.OrderDTO;
import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.responses.order.OrderListResponse;
import com.project.shopapp.responses.order.OrderResponse;
import com.project.shopapp.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    final private IOrderService orderService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result
    ) throws Exception {
            if (result.hasErrors()) {
                List<String> errorMsg = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ResponseObject.builder()
                                .message(errorMsg.toString())
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }

            OrderResponse orderResponse = orderService.createOrder(orderDTO);

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Order created")
                            .status(HttpStatus.OK)
                            .data(orderResponse)
                            .build()
            );

    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<ResponseObject> getOrders(@Valid @PathVariable("user_id") Long userId) {
            List<OrderResponse> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .data(orders)
                            .status(HttpStatus.OK)
                            .message("Orders retrieved")
                            .build()
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrder(@Valid @PathVariable("id") Long id) {
            OrderResponse order = orderService.getOrder(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .data(order)
                            .status(HttpStatus.OK)
                            .message("Order retrieved")
                            .build()
            );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted");
    }

    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListResponse> getOrdersByKeyword(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

            Page<OrderResponse> orders = orderService.getOrdersByKeyword(keyword, pageRequest);

            return ResponseEntity.ok(new OrderListResponse(orders.getContent(), orders.getTotalPages()));
    }
}
