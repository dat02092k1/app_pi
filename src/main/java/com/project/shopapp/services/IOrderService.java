package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.responses.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;

    OrderResponse getOrder(Long id);

    OrderResponse updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<OrderResponse> findByUserId(Long userId);

    Page<OrderResponse> getOrdersByKeyword(String keyword, Pageable pageable);
}
