package com.example.shoppingverse.transformer;

import com.example.shoppingverse.dto.response.ItemResponseDto;
import com.example.shoppingverse.dto.response.OrderResponseDto;
import com.example.shoppingverse.dto.response.TopOrderTotalResponseDto;
import com.example.shoppingverse.model.Item;
import com.example.shoppingverse.model.OrderEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderTransformer {
    public static OrderResponseDto OrderToOrderResponseDto(OrderEntity order){
        List<ItemResponseDto> list=new ArrayList<>();
        for(Item item: order.getItems()){
            list.add(ItemTransformer.ItemToItemResponseDto(item));
        }
       return OrderResponseDto.builder()
               .orderId(order.getOrderId())
               .orderDate(order.getOrderDate())
               .cardUsed(order.getCardUsed())
               .orderTotal(order.getOrderTotal())
               .customerName(order.getCustomer().getName())
               .item(list)
               .build();

    }
    public static TopOrderTotalResponseDto OrderToTopOrderTotalResponseDto(OrderEntity order){
           TopOrderTotalResponseDto response=TopOrderTotalResponseDto.builder()
                   .orderId(order.getOrderId())
                   .orderDate(order.getOrderDate())
                   .cardUsed(order.getCardUsed())
                   .orderTotal(order.getOrderTotal())
                   .customerName(order.getCustomer().getName())
                   .build();
           return response;

    }
}
