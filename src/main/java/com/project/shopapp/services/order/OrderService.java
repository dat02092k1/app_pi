package com.project.shopapp.services.order;

import com.project.shopapp.dtos.order.CartItemDTO;
import com.project.shopapp.dtos.order.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.order.OrderResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // check if user id in orderDTO exists in the database
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(
                        () -> new DataNotFoundException("User with id " + orderDTO.getUserId() + " not found")
                );

        // convert orderDTO to Order object
        // Model mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();

        if (shippingDate.isBefore(LocalDate.now())) {
            throw new Exception("Invalid shipping date");
        }
        order.setShippingDate((shippingDate));
        orderRepository.save(order);

        // create list order detail from cart items
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(
                            () -> new DataNotFoundException("Product with id " + productId + " not found")
                    );
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice() * quantity);
            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order optionalOrder = orderRepository.findById(id).orElse(null);
        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(optionalOrder, OrderResponse.class);

    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order with id " + orderId + " not found"));

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User with id " + orderDTO.getUserId() + " not found"));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        modelMapper.map(orderDTO, existingOrder);

        existingOrder.setUser(existingUser);

        orderRepository.save(existingOrder);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(existingOrder, OrderResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order optionalOrder = orderRepository.findById(id).orElse(null);

        if (optionalOrder != null) {
            optionalOrder.setActive(false);
            orderRepository.save(optionalOrder);
        }

    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public Page<OrderResponse> getOrdersByKeyword(String keyword, Pageable pageable) {
        Page<Order> orders = orderRepository.findByKeyword(keyword, pageable);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return orders.map(order -> modelMapper.map(order, OrderResponse.class));
    }
}
