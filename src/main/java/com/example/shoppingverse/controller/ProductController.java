package com.example.shoppingverse.controller;

import com.example.shoppingverse.Enum.ProductCategory;
import com.example.shoppingverse.dto.request.ProductRequestDto;
import com.example.shoppingverse.dto.response.ItemResponseDto;
import com.example.shoppingverse.dto.response.ProductResponseDto;
import com.example.shoppingverse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequestDto productRequestDto){
        try{
            ProductResponseDto responseDto=productService.addProduct(productRequestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_by_category_and_price_greater_than")
    public ResponseEntity getProductsByCategoryAndPriceGreaterThan(@RequestParam int price, @RequestParam ProductCategory category){
       List<ProductResponseDto> productResponseDtoList=
               productService.getProductsByCategoryAndPriceGreaterThan(price,category);
       return new ResponseEntity(productResponseDtoList,HttpStatus.FOUND);

    }
//
    @GetMapping("/get_top_cheapest_product")
    public  ResponseEntity getTopCheapestProductInAGivenCategory(@RequestParam int n,@RequestParam ProductCategory category){
        try{
            List<ProductResponseDto> responseDtoList=productService.getTopCheapestProductInAGivenCategory(n,category);
            return new ResponseEntity(responseDtoList,HttpStatus.FOUND);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }



    }
    @GetMapping("/get_items")
    public ResponseEntity getItemsOfAGivenProduct(@RequestParam int id){
        List<ItemResponseDto> responseDtoList=productService.getItemsOfAGivenProduct(id);
        return new ResponseEntity(responseDtoList,HttpStatus.FOUND);
    }

}
