package com.example.shoppingverse.dto.response;

import com.example.shoppingverse.Enum.ProductCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ItemResponseDto {
    int quantityAdded;
    String itemName;
    int itemPrice;
    ProductCategory category;
}
