package com.example.shoppingverse.dto.request;

import com.example.shoppingverse.Enum.CardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CardRequestDto {
    String customerEmail;
    String cardNo;

    int cvv;
    Date validTill;

    CardType cardType;
}
