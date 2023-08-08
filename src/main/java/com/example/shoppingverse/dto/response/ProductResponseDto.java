package com.example.shoppingverse.dto.response;

import com.example.shoppingverse.Enum.ProductCategory;
import com.example.shoppingverse.Enum.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ProductResponseDto {
    String sellerName;
    int price;
    String productName;
    int availableQuantity;
    ProductStatus status;
    ProductCategory category;
}
