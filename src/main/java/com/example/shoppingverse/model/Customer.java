package com.example.shoppingverse.model;

import com.example.shoppingverse.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "customer_table")
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    @Column(unique = true)
    String emailId;
    @Column(unique = true)
    String mobileNo;
    @Enumerated(value = EnumType.STRING)
    Gender gender;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    Cart cart;//custoemr to cart one to one ,customer is parent
   @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    List<Card>cards=new ArrayList<>();
   @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
   List<OrderEntity> orders=new ArrayList<>();
}
