package com.example.shoppingverse.service;

import com.example.shoppingverse.dto.request.CardRequestDto;
import com.example.shoppingverse.dto.response.CardResponseDto;
import com.example.shoppingverse.exception.CustomerNotFoundException;
import com.example.shoppingverse.model.Card;
import com.example.shoppingverse.model.Customer;
import com.example.shoppingverse.repository.CustomerRepository;
import com.example.shoppingverse.transformer.CardTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    CustomerRepository customerRepository;

    public CardResponseDto addCard(CardRequestDto cardRequestDto) {

        Customer customer=customerRepository.findByEmailId(cardRequestDto.getCustomerEmail());
        if(customer==null) throw new CustomerNotFoundException("customer doesn't exist");

        //dto to entity
        Card card= CardTransformer.CardRequestDtoToCard(cardRequestDto);
        card.setCustomer(customer);
        customer.getCards().add(card);
        //save to customer as it is the parent
        Customer savedCustomer=customerRepository.save(customer);
        //get the latest card saved in customer class
        List<Card> cardList=savedCustomer.getCards();
        Card latestSavedCArd=cardList.get(cardList.size()-1);
        //entity to responseDto
        CardResponseDto responseDto=CardTransformer.CardToCardResponseDto(latestSavedCArd);
        responseDto.setCardNo(generateMaskedNo(latestSavedCArd.getCardNo()));
        return responseDto;

    }
    public  String generateMaskedNo(String str){
        String ans="";
        for(int i=0;i<str.length()-4;i++){
            ans+="x";
        }
        ans+=str.substring(str.length()-4);
        return ans;
    }
}
