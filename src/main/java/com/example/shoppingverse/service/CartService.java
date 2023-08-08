package com.example.shoppingverse.service;

import com.example.shoppingverse.dto.request.CheckOutCartRequestDto;
import com.example.shoppingverse.dto.request.ItemRequestDto;
import com.example.shoppingverse.dto.response.CartResponseDto;
import com.example.shoppingverse.dto.response.OrderResponseDto;
import com.example.shoppingverse.exception.CustomerNotFoundException;
import com.example.shoppingverse.exception.EmptyCartException;
import com.example.shoppingverse.exception.InvalidCardException;
import com.example.shoppingverse.model.*;
import com.example.shoppingverse.repository.*;
import com.example.shoppingverse.transformer.CartTransformer;
import com.example.shoppingverse.transformer.OrderTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class CartService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderEntityRepository orderEntityRepository;


    public CartResponseDto addItemToCart(ItemRequestDto itemRequestDto,Item item) {
        //get the cart of customer to which you want to add
        Customer customer=customerRepository.findByEmailId(itemRequestDto.getCustomerEmail());
        Product product=productRepository.findById(itemRequestDto.getProductId()).get();

      //product can be fetced from item object
        Cart cart=customer.getCart();
        cart.setCartTotal(cart.getCartTotal()+ itemRequestDto.getRequiredQuantity()* product.getPrice());

        item.setCart(cart);
        item.setProduct(product);
        Item savedItem=itemRepository.save(item);//to avoid duplicacy

        cart.getItems().add(savedItem);
        product.getItems().add(savedItem);
        Cart savedCart=cartRepository.save(cart);
        productRepository.save(product);
        //prepare response dto
        return CartTransformer.CartToCartResponseDto(savedCart);


    }

    public OrderResponseDto checkOutCart(CheckOutCartRequestDto checkOutCartRequestDto) throws Exception {
        Customer customer=customerRepository.findByEmailId(checkOutCartRequestDto.getCustomerEmail());
        if(customer==null)
            throw new CustomerNotFoundException("Customer doesn't exist");
        Card card=cardRepository.findByCardNo(checkOutCartRequestDto.getCardNo());
        Date currentDate=new Date();
        if(card==null||card.getCvv()!= checkOutCartRequestDto.getCvv()||currentDate.after(card.getValidTill()))
            throw new InvalidCardException("Card is not valid");

        //check whether cart is empty or not
        Cart cart=customer.getCart();
        if(cart.getItems().size()==0)
            throw new EmptyCartException("Sorry!Cart is empty");

       OrderEntity order=orderService.placeOrder(cart,card);
       //reset the cart
        reset(cart);
        OrderEntity savedOrder=orderEntityRepository.save(order);
        return OrderTransformer.OrderToOrderResponseDto(savedOrder);


    }
    public void reset(Cart cart){
        cart.setCartTotal(0);
        for(Item item:cart.getItems()){
            item.setCart(null);
        }
        cart.setItems(new ArrayList<>());

    }
}
