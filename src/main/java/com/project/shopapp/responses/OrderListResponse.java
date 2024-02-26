package com.project.shopapp.responses;

import com.project.shopapp.models.Order;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPages;

}
