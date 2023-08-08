package com.example.shoppingverse.service;

import com.example.shoppingverse.dto.request.SellerRequestDto;
import com.example.shoppingverse.dto.response.SellerResponseDto;
import com.example.shoppingverse.model.Product;
import com.example.shoppingverse.model.Seller;
import com.example.shoppingverse.repository.ProductRepository;
import com.example.shoppingverse.repository.SellerRepository;
import com.example.shoppingverse.transformer.SellerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerService {
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ProductRepository productRepository;
    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) {
        //dto->Entity
        Seller seller= SellerTransformer.SellerRequestDtoToSeller(sellerRequestDto);
        //saving seller
        Seller savedSeller=sellerRepository.save(seller);
        //saved entity to dto
        SellerResponseDto responseDto=SellerTransformer.SellerToSellerResponseDto(savedSeller);

        return responseDto;
    }

    public List<SellerResponseDto> cheapSeller() {
        List<Product> productList=productRepository.productsByPriceAscending();
        int cheapPrice=productList.get(0).getPrice();
        List<SellerResponseDto> ansList=new ArrayList<>();
        for(Product product:productList){
            if(product.getPrice()==cheapPrice){
                int sellerId=product.getSeller().getId();
                Seller seller=sellerRepository.findById(sellerId).get();
                ansList.add(SellerTransformer.SellerToSellerResponseDto(seller));

            }
        }
        return ansList;
    }

}
