package com.project.shopapp.responses;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
