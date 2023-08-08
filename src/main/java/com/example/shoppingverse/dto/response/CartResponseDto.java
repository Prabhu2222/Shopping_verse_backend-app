package com.example.shoppingverse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CartResponseDto {
    String customerName;
    int cartTotal;
    //never expose total entityso instead of
    //showing List<Item> we used
    List<ItemResponseDto> items;

}
