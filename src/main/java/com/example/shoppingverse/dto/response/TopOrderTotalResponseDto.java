package com.example.shoppingverse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TopOrderTotalResponseDto {
    String orderId;//uuid
    Date orderDate;

    String cardUsed;
    int orderTotal;
    String customerName;
}
