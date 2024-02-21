package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {
        Order order = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(
                        () -> new DataNotFoundException("Order not found")
                );

        Product product = productRepository.findById(newOrderDetail.getProductId())
                .orElseThrow(
                        () -> new DataNotFoundException("Product not found")
                );

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(newOrderDetail.getPrice())
                .numberOfProducts(newOrderDetail.getNumberOfProducts())
                .totalMoney(newOrderDetail.getTotalMoney())
                .color(newOrderDetail.getColor())
                .build();

        OrderDetail orderDetail1 = orderDetailRepository.save(orderDetail);

        return OrderDetailResponse.fromOrderDetail(orderDetail1);
    }

    @Override
    public OrderDetail getOrderDetailById(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException("Order detail not found")
                );
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(
                        () -> new DataNotFoundException("Order detail not found")
                );

        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(
                        () -> new DataNotFoundException("Order not found")
                );

        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(
                        () -> new DataNotFoundException("Product not found")
                );

        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
