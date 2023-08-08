package com.example.shoppingverse.repository;

import com.example.shoppingverse.Enum.ProductCategory;
import com.example.shoppingverse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query(value="select p from Product p where p.price>=:price and p.category= :category")
    List<Product> getProductsByCategoryAndPriceGreaterThan(int price, ProductCategory category);
//   @Query(value="select * from product_table where category=:category ",nativeQuery = true)
//    List<Product> getTop5(ProductCategory category);

   @Query(value="select * from product_table  where category=:category order by price limit :n",nativeQuery = true)
    List<Product> getTopCheapestProductInAGivenCategory(int n, ProductCategory category);
 @Query(value = "select * from product_table order by price",nativeQuery = true)
    List<Product> productsByPriceAscending();
}
