package com.example.shoppingverse.service;

import com.example.shoppingverse.Enum.Gender;
import com.example.shoppingverse.dto.request.CustomerRequestDto;
import com.example.shoppingverse.dto.response.CustomerResponseDto;
import com.example.shoppingverse.model.Cart;
import com.example.shoppingverse.model.Customer;
import com.example.shoppingverse.repository.CustomerRepository;
import com.example.shoppingverse.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) {
        //dto->entity
//        Customer customer=new Customer();
//        customer.setName(customerRequestDto.getName());
//        customer.setGender(customerRequestDto.getGender());
//        customer.setEmailId(customerRequestDto.getEmailId());
//        customer.setMobileNo(customerRequestDto.getMobileNo());
        //using transformers //dto->entity
        Customer customer=CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);

        Cart cart=new Cart();
        cart.setCartTotal(0);
        cart.setCustomer(customer);
        customer.setCart(cart);
        Customer savedCustomer=customerRepository.save(customer);

        CustomerResponseDto responseDto=CustomerTransformer.CustomerToCustomerResponseDto(savedCustomer);
        return responseDto;


    }

    public List<Customer> getFemaleCustomer(int orderQuantityK, Gender gender) {
         List<Customer>  list=customerRepository. getFemaleCustomer(orderQuantityK,gender);
         List<Customer> ans=new ArrayList<>();
         for(Customer ele:list){
             if(ele.getOrders().size()>=orderQuantityK)
                 ans.add(ele);
         }
         return ans;

    }

    public List<CustomerResponseDto> getFemalesAtLeastKOrders(int k) {
        List<Customer> customerList=customerRepository.findAll();
        List<CustomerResponseDto> ansList=new ArrayList<>();
        for(Customer customer:customerList){
            if(customer.getGender().equals(Gender.FEMALE) && customer.getOrders().size()>=k)
                ansList.add(CustomerTransformer.CustomerToCustomerResponseDto(customer));
        }
        return ansList;
    }
}
