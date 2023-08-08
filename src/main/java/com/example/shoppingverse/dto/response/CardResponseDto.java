package com.example.shoppingverse.dto.response;

import com.example.shoppingverse.Enum.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CardResponseDto {
    String customerName;
    String cardNo;//masked card no

    Date validTill;

    CardType cardType;
}
