package com.example.shoppingverse.exception;

public class NotEnoughOrderException extends RuntimeException{
    public NotEnoughOrderException(String msg){
        super(msg);
    }
}
