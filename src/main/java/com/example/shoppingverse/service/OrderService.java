package com.example.shoppingverse.service;

import com.example.shoppingverse.Enum.ProductStatus;
import com.example.shoppingverse.dto.request.OrderRequestDto;
import com.example.shoppingverse.dto.response.CustomerResponseDto;
import com.example.shoppingverse.dto.response.OrderResponseDto;
import com.example.shoppingverse.dto.response.TopOrderTotalResponseDto;
import com.example.shoppingverse.exception.*;
import com.example.shoppingverse.model.*;
import com.example.shoppingverse.repository.CardRepository;
import com.example.shoppingverse.repository.CustomerRepository;
import com.example.shoppingverse.repository.OrderEntityRepository;
import com.example.shoppingverse.repository.ProductRepository;
import com.example.shoppingverse.transformer.ItemTransformer;
import com.example.shoppingverse.transformer.OrderTransformer;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardService cardService;
    @Autowired
    OrderEntityRepository orderEntityRepository;
    @Autowired
    JavaMailSender javaMailSender;
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) throws Exception {
        //check whether customer exists or not
        Customer customer=customerRepository.findByEmailId(orderRequestDto.getCustomerEmail());
        if(customer==null) throw new CustomerNotFoundException("Customer doesn't exist");

        //check whether the selected product exists or not products exists or
        Optional<Product> optionalProduct=productRepository.findById(orderRequestDto.getProductId());
        if(optionalProduct.isEmpty()) throw new ProductNotFoundException("product doesn't exist");

        //check card and its validity
        Card card=cardRepository.findByCardNo(orderRequestDto.getCardUsed());
        Date todayDate=new Date();
        if (card == null || card.getCvv() != orderRequestDto.getCvv() || todayDate.after(card.getValidTill())) {
            throw new InvalidCardException("Card is invalid");
        }

        //check whether order quantity is available or not
        Product product=optionalProduct.get();
//        System.out.println(product.getAvailableQuantity());
//        System.out.println(orderRequestDto.getOrderQuantity());
        if(product.getAvailableQuantity()< orderRequestDto.getOrderQuantity()){
            throw new InsufficientQuantityException("Insufficient quantity available");
        }

        int newQuantity= product.getAvailableQuantity()-orderRequestDto.getOrderQuantity();
        System.out.println(newQuantity);
        product.setAvailableQuantity(newQuantity);
        if(newQuantity==0){
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }
        //now preapare orderEntity,create order
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setOrderId(String.valueOf(UUID.randomUUID()));
        orderEntity.setCardUsed(cardService.generateMaskedNo(orderRequestDto.getCardUsed()));
        orderEntity.setOrderTotal(orderRequestDto.getOrderQuantity()*product.getPrice());
        //create and add the item;
        Item item= ItemTransformer.ItemRequestDtoToItem(orderRequestDto.getOrderQuantity());
        //we dont need to set cart coz this is a direct order
        item.setOrderEntity(orderEntity);
        item.setProduct(product);

        orderEntity.getItems().add(item);
        orderEntity.setCustomer(customer);
        //save order
        OrderEntity savedOrder=orderEntityRepository.save(orderEntity);
        //here entities involved are order,item,customer,product.order and item are set now check inn customer
        //and product which variables need to be set
        product.getItems().add(savedOrder.getItems().get(0));//product entity resolved
        customer.getOrders().add(savedOrder);//customer entity resolved

        //save the parent when parent does not in the database.here both customer and product are already saved with primary key
        //in the database.so if you save it agian it might end up with infinite recursion/loop for e. g here if we save the product again
        //product->item->cart->customer->cart->customer....so csrt to customer ininite loop while saving.
        //conclusion in multiple entity scenario
        //identify which entity already got the primary key(here it is customer and product)
        //then for those entitiy get th reference by e.g.product p=productRpo.findById();this p object will be referrring to db
        //table so whatever you update in p it will be reflected in db.
        //now for unsaved things check which is parent and which is child.if it is bidirectional saving parents will
        //save the child else save both of them .here in unsaved thing i am talking about those entities
        //whose primary keys has not been saved.
//        productRepository.save(product);
//        customerRepository.save(customer);

        OrderResponseDto orderResponseDto= OrderTransformer.OrderToOrderResponseDto(savedOrder);
        //sending mail
        sendOrderConfirmedMail(orderResponseDto,orderEntity);
        return orderResponseDto;


    }



    public OrderEntity placeOrder(Cart cart, Card card) throws Exception{

        OrderEntity order=new OrderEntity();
        order.setOrderId(String.valueOf(UUID.randomUUID()));
        order.setCardUsed(cardService.generateMaskedNo(card.getCardNo()));

        int orderTotal=0;
        for(Item item: cart.getItems()){
            Product product=item.getProduct();
            if(product.getAvailableQuantity()< item.getRequiredQuantity())
                throw new InsufficientQuantityException("Sorry !Insufficient quantity  available for: "+product.getProductName());
            int newQuantity=product.getAvailableQuantity()-item.getRequiredQuantity();
            product.setAvailableQuantity(newQuantity);
            if(newQuantity==0) product.setStatus(ProductStatus.OUT_OF_STOCK);

            orderTotal+= product.getPrice()*item.getRequiredQuantity();
            item.setOrderEntity(order);
        }
        order.setOrderTotal(orderTotal);
        order.setItems(cart.getItems());
        order.setCustomer(cart.getCustomer());
        //send mail
        sendCartOrderPlaceMail(order);
        return order;
    }

    public List<TopOrderTotalResponseDto> getTopOrderTotal(int n) {
         List<OrderEntity> list= orderEntityRepository. getTopOrderTotal(n);
         if(list.size()<n) throw new NotEnoughOrderException(String.format("There is less than %d orders",n));
         int count=0;
         List<TopOrderTotalResponseDto> ansList=new ArrayList<>();
         for(OrderEntity ele:list){
             ansList.add(OrderTransformer.OrderToTopOrderTotalResponseDto(ele));
             count++;
             if(count==n) break;

         }
         return ansList;
    }


    public String cancelOrder(String orderId) throws Exception {
        OrderEntity order=orderEntityRepository.findByOrderId(orderId);
        if(order==null) throw new InvalidOrderIdException("No such orderId exists");
        //make order total as 0 an orderid as null
        sendCancelOrderMail(order);
        order.setOrderId(null);
        order.setOrderTotal(0);
      //save it to the repository

        List<Item> itemList=order.getItems();

        for(Item item:itemList){
            //make orderid null in item table
            item.setOrderEntity(null);
            //add the corresponsding product in the product table
            int productId=item.getProduct().getId();
            //update the status of the product as available
            Product product=item.getProduct();
            int amountBefore=product.getAvailableQuantity();
            int amountAfterAddition=amountBefore+ item.getRequiredQuantity();
            item.setRequiredQuantity(0);
            product.setAvailableQuantity(amountAfterAddition);
            if(product.getAvailableQuantity()>0) product.setStatus(ProductStatus.AVAILABLE);
            productRepository.save(product);
        }
        orderEntityRepository.save(order);
        return "order Cancelled";
    }
    public void sendOrderConfirmedMail(OrderResponseDto orderResponseDto,OrderEntity order) throws Exception{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        helper.setFrom("pojectbro@gmail.com");
        helper.setTo(order.getCustomer().getEmailId());
        String subject="Congratulations!!! "+order.getCustomer().getName()+" your order has been placed." +
                " Followings are the details:\n"+
                "orderId: "+order.getOrderId()+"\n" +
                "orderDate: "+order.getOrderDate()+"\n"+
                "cardNumber: "+order.getCardUsed()+"\n"+
                "order Total: "+order.getOrderTotal()+"\n"+
                "orderItems Details: \n"+
                "item Name-> "+orderResponseDto.getItem().get(0).getItemName()+"\n"+
                "item Category-> "+orderResponseDto.getItem().get(0).getCategory()+"\n"+
                 "item price-> "+orderResponseDto.getItem().get(0).getItemPrice()+"\n"+
                "quantity Ordered-> "+orderResponseDto.getItem().get(0).getQuantityAdded();

        helper.setText(subject);
        FileSystemResource File1=new FileSystemResource(new File("src/main/java/com/example/shoppingverse/MailImages/orderConfirm.jpeg"));
        FileSystemResource File2=new FileSystemResource(new File("src/main/java/com/example/shoppingverse/MailImages/ThankYouForOrder.jpeg"));
        helper.addAttachment("image.jpeg",File1);
        helper.addAttachment("image.jpeg",File2);
        javaMailSender.send(message);

    }
    public void sendCartOrderPlaceMail(OrderEntity order) throws Exception{

        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        helper.setFrom("pojectbro@gmail.com");
        helper.setTo(order.getCustomer().getEmailId());
        String subject="Congratulations!!! "+order.getCustomer().getName()+" your cart order has been placed." +
                " Followings are the details:\n"+
                "orderId: "+order.getOrderId()+"\n" +
                "orderDate: "+order.getOrderDate()+"\n"+
                "cardNumber: "+order.getCardUsed()+"\n"+
                "order Total: "+order.getOrderTotal()+"\n";
        helper.setText(subject);
        FileSystemResource File1=new FileSystemResource(new File("src/main/java/com/example/shoppingverse/MailImages/orderConfirm.jpeg"));
        FileSystemResource File2=new FileSystemResource(new File("src/main/java/com/example/shoppingverse/MailImages/ThankYouForOrder.jpeg"));
        helper.addAttachment("image.jpeg",File1);
        helper.addAttachment("image.jpeg",File2);
        javaMailSender.send(message);

    }
    public void sendCancelOrderMail(OrderEntity order) throws Exception{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("pojectbro@gmail.com");
        helper.setTo(order.getCustomer().getEmailId());
        String subject="Oops!!! "+order.getCustomer().getName()+". Your  order having \n order Id: "+order.getOrderId() + "\n has been canceled."
                +" Thank you for shopping with us.";
        helper.setText(subject);
        FileSystemResource File1=new FileSystemResource(new File("src/main/java/com/example/shoppingverse/MailImages/CancellationConfirmed.png"));
        helper.addAttachment("image.png",File1);
        javaMailSender.send(message);
    }
}
