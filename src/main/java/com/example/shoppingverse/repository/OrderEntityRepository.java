package com.example.shoppingverse.repository;

import com.example.shoppingverse.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderEntityRepository extends JpaRepository<OrderEntity,Integer> {

    @Query(value="select * from order_info_table order by order_total desc limit :n",nativeQuery = true)
    List<OrderEntity> getTopOrderTotal(int n);

    OrderEntity findByOrderId(String orderId);
}
