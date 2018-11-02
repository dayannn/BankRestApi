package com.dayannn.bankrestapi.Exceptions;

public class AccountNotFoundException extends  RuntimeException{
    public AccountNotFoundException(Integer id) {
        super("Could not find employee " + id);
    }
}
