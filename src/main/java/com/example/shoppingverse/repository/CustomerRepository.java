package com.example.shoppingverse.repository;

import com.example.shoppingverse.Enum.Gender;
import com.example.shoppingverse.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Customer findByEmailId(String customerEmail);

    @Query(value="select * from customer_table where gender=\"FEMALE\" and age between 25 and 30",nativeQuery = true)
    List<Customer> getFemaleCustomer(int orderQuantityK, Gender gender);
}
