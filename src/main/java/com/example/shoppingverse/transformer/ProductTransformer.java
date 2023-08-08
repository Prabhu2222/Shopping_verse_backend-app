package com.example.shoppingverse.transformer;

import com.example.shoppingverse.Enum.ProductStatus;
import com.example.shoppingverse.dto.request.ProductRequestDto;
import com.example.shoppingverse.dto.response.ProductResponseDto;
import com.example.shoppingverse.model.Product;

public class ProductTransformer {
    public static Product ProductRequestDtoToProduct(ProductRequestDto productRequestDto){
        Product product=Product.builder()
                .productName(productRequestDto.getProductName())
                .availableQuantity(productRequestDto.getAvailableQuantity())
                .price(productRequestDto.getPrice())
                .category(productRequestDto.getCategory())
                .status(ProductStatus.AVAILABLE)
                .build();
        return product;

    }
    public static ProductResponseDto ProductToProductResponseDto(Product product){
        ProductResponseDto responseDto=ProductResponseDto.builder()
                .sellerName(product.getSeller().getName())
                .price(product.getPrice())
                .productName(product.getProductName())
                .availableQuantity(product.getAvailableQuantity())
                .status(product.getStatus())
                .category(product.getCategory())
                .build();
        return responseDto;
    }
}
