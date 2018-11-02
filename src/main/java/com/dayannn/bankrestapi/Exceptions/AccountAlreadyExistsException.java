package com.dayannn.bankrestapi.Exceptions;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(Integer id) {
        super("Couldn't create an account with id = " + id + ". It already exists");
    }
}
