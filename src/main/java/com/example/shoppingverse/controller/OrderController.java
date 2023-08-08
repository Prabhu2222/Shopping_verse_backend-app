package com.example.shoppingverse.controller;

import com.example.shoppingverse.dto.request.OrderRequestDto;
import com.example.shoppingverse.dto.response.CustomerResponseDto;
import com.example.shoppingverse.dto.response.OrderResponseDto;
import com.example.shoppingverse.dto.response.TopOrderTotalResponseDto;
import com.example.shoppingverse.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
@PostMapping("/place")
    public ResponseEntity placeOrder(@RequestBody OrderRequestDto orderRequestDto){
        try{
            OrderResponseDto response=orderService.placeOrder(orderRequestDto);
            return new ResponseEntity(response,HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/get_top_order_total")//problems in mapping
    public ResponseEntity getTopOrderTotal(@RequestParam int n){
    try{
        List<TopOrderTotalResponseDto> responseDto=orderService.getTopOrderTotal(n);
        return new ResponseEntity(responseDto,HttpStatus.FOUND);
    }catch(Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }


    }
    @PutMapping("/cancel")
    public ResponseEntity cancelOrder(@RequestParam String orderId){
      try{
         String response=orderService.cancelOrder(orderId);
         return new ResponseEntity<>(response,HttpStatus.FOUND);
      }catch (Exception e){
          return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
      }

    }




}
