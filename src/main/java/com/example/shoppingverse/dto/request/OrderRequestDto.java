package com.example.shoppingverse.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class OrderRequestDto {
    String customerEmail;
    int productId;
    String cardUsed;
    int cvv;
    int orderQuantity;
}
