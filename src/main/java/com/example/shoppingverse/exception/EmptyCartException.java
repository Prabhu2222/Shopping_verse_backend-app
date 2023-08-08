package com.example.shoppingverse.exception;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String msg){
        super(msg);
    }
}
