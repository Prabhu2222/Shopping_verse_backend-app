package com.example.shoppingverse.controller;

import com.example.shoppingverse.Enum.Gender;
import com.example.shoppingverse.dto.request.CustomerRequestDto;
import com.example.shoppingverse.dto.response.CustomerResponseDto;
import com.example.shoppingverse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody CustomerRequestDto customerRequestDto){
        CustomerResponseDto response=customerService.addCustomer(customerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/get_females_at_least_k_orders")
    public ResponseEntity getFemalesAtLeastKOrders(@RequestParam int k){
        List<CustomerResponseDto> responseDtoList=customerService.getFemalesAtLeastKOrders(k);
        return new ResponseEntity<>(responseDtoList,HttpStatus.FOUND);

    }

}
