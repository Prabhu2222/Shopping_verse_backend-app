package com.example.shoppingverse.service;

import com.example.shoppingverse.Enum.ProductCategory;
import com.example.shoppingverse.dto.request.ProductRequestDto;
import com.example.shoppingverse.dto.response.ItemResponseDto;
import com.example.shoppingverse.dto.response.ProductResponseDto;
import com.example.shoppingverse.exception.NotEnoughProductException;
import com.example.shoppingverse.exception.SellerNotFoundException;
import com.example.shoppingverse.model.Item;
import com.example.shoppingverse.model.Product;
import com.example.shoppingverse.model.Seller;
import com.example.shoppingverse.repository.ProductRepository;
import com.example.shoppingverse.repository.SellerRepository;
import com.example.shoppingverse.transformer.ItemTransformer;
import com.example.shoppingverse.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Seller seller=sellerRepository.findByEmailId(productRequestDto.getSellerEmail());
//        System.out.println(seller.getName());
//        System.out.println(seller.getEmailId());
        if(seller==null)throw new SellerNotFoundException("Seller doesn't exist");

        //dto->entity
        Product product= ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        product.setSeller(seller);
        seller.getProducts().add(product);
        //save both by saving only parent
        Seller savedSeller=sellerRepository.save(seller);
        List<Product> productList=savedSeller.getProducts();
        Product latestProduct=productList.get(productList.size()-1);
        //Entity->dto
        return ProductTransformer.ProductToProductResponseDto(latestProduct);

    }

    public List<ProductResponseDto> getProductsByCategoryAndPriceGreaterThan(int price, ProductCategory category) {

        List<Product> productsList=productRepository.getProductsByCategoryAndPriceGreaterThan(price,category);
        List<ProductResponseDto>list=new ArrayList<>();
        for(Product ele:productsList){
            list.add(ProductTransformer.ProductToProductResponseDto(ele));
        }
        return list;
    }

    public List<ProductResponseDto> getTopCheapestProductInAGivenCategory(int n,ProductCategory category) {
        System.out.println("i m top");
        List<Product> productList=productRepository.getTopCheapestProductInAGivenCategory(n,category);
        System.out.println("i am here");
        if(productList.size()<n) throw new NotEnoughProductException(String.format("there are less than %d products in the given category",n));
        int count=0;
        List<ProductResponseDto> ansList=new ArrayList<>();
        for(Product ele:productList){
            ansList.add(ProductTransformer.ProductToProductResponseDto(ele));
            count++;
            if(count==n)break;
        }
        return ansList;
    }

    public List<ItemResponseDto> getItemsOfAGivenProduct(int id) {
        Product product=productRepository.findById(id).get();
        List<ItemResponseDto> ansList=new ArrayList<>();
        for(Item item:product.getItems()){
            ansList.add(ItemTransformer.ItemToItemResponseDto(item));
        }
        return ansList;
    }
}
