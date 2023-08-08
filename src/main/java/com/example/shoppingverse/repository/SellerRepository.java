package com.example.shoppingverse.repository;

import com.example.shoppingverse.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Integer> {
    @Query(value="select * from seller_table where email_id=:sellerEmail",nativeQuery = true)
    public Seller findByEmailId(String sellerEmail);

    @Query(value="select * form seller_table",nativeQuery = true)
    List<Seller> cheapSeller();
}
