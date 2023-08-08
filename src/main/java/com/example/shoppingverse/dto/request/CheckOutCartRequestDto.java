package com.example.shoppingverse.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CheckOutCartRequestDto {
    String customerEmail;
    String cardNo;
    int cvv;
}
