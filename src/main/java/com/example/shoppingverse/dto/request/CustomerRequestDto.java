package com.example.shoppingverse.dto.request;

import com.example.shoppingverse.Enum.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class CustomerRequestDto {
    String name;

    String emailId;

    String mobileNo;

    Gender gender;
}
