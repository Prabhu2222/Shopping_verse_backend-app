package com.example.shoppingverse.exception;

public class InvalidCardException extends RuntimeException{
    public InvalidCardException(String msg){
        super(msg);
    }
}
