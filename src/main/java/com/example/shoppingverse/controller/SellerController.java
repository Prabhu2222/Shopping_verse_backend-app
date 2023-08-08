package com.example.shoppingverse.controller;

import com.example.shoppingverse.dto.request.SellerRequestDto;
import com.example.shoppingverse.dto.response.SellerResponseDto;
import com.example.shoppingverse.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;
    @PostMapping("/add")
    public ResponseEntity addSeller(@RequestBody SellerRequestDto sellerRequestDto){
        SellerResponseDto response=sellerService.addSeller(sellerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @GetMapping("/get_cheap_seller")
    public ResponseEntity cheapSeller(){
        List<SellerResponseDto> responseDtoList=sellerService.cheapSeller();
        return new ResponseEntity(responseDtoList,HttpStatus.FOUND);

    }
}
