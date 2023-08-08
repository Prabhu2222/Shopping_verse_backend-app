package com.example.shoppingverse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class OrderResponseDto {
    String orderId;//uuid
    Date orderDate;

    String cardUsed;
    int orderTotal;
    String customerName;
    List<ItemResponseDto> item;
}
