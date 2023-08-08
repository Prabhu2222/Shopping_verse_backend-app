package com.example.shoppingverse.exception;

public class InvalidOrderIdException extends RuntimeException{
    public InvalidOrderIdException(String message){
        super(message);
    }
}
