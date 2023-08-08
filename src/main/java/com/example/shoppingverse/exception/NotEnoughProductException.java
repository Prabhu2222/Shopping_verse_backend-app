package com.example.shoppingverse.exception;

public class NotEnoughProductException extends RuntimeException{
    public NotEnoughProductException(String message){
        super(message);
    }
}
