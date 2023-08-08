package com.example.shoppingverse.transformer;

import com.example.shoppingverse.dto.request.CustomerRequestDto;
import com.example.shoppingverse.dto.response.CustomerResponseDto;
import com.example.shoppingverse.model.Customer;

public class CustomerTransformer {
    //this is a utility class which have only methods

    public static Customer CustomerRequestDtoToCustomer(CustomerRequestDto customerRequestDto){
        Customer customer=Customer.builder()
                .name(customerRequestDto.getName())
                .emailId(customerRequestDto.getEmailId())
                .mobileNo(customerRequestDto.getMobileNo())
                .gender(customerRequestDto.getGender())
                .build();
        return customer;
    }
    public  static CustomerResponseDto CustomerToCustomerResponseDto(Customer customer){
        CustomerResponseDto customerResponseDto=CustomerResponseDto.builder()
                .name(customer.getName())
                .emailId(customer.getEmailId())
                .mobileNo(customer.getMobileNo())
                .gender(customer.getGender())
                .build();
        return customerResponseDto;
    }
}
