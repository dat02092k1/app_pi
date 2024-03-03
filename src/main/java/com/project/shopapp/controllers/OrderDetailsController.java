package com.project.shopapp.controllers;

import com.project.shopapp.dtos.order_detail.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.responses.order.OrderDetailResponse;
import com.project.shopapp.services.order_detail.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailsController {
    private final IOrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailsDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMsg = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(errorMsg.toString())
                            .data(null)
                            .build()
            );
        }

            OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailsDTO);

            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .status(HttpStatus.CREATED)
                            .message("Order detail created")
                            .data(orderDetailResponse)
                            .build()
            );

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("id") Long id) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get list order details of an order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponseList = orderDetails.stream().map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateOrderDetails(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailsDTO
    ) throws DataNotFoundException, Exception {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailsDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .status(HttpStatus.OK)
                            .message("Order detail updated")
                            .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                            .build()
            );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetails(@Valid @PathVariable("id") Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("Order detail deleted");
    }

}
