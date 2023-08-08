package com.example.shoppingverse.model;

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
@Table(name = "item_table")
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int requiredQuantity;
    @ManyToOne
    @JoinColumn
    Cart cart;//item to cart is many to one ,item is the child
    @ManyToOne
    @JoinColumn
    OrderEntity orderEntity;//orderEntity to item is one to many and item is child

    @ManyToOne
    @JoinColumn
    Product product;
}
