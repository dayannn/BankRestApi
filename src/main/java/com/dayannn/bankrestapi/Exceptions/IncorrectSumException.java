package com.dayannn.bankrestapi.Exceptions;

public class IncorrectSumException extends RuntimeException {
    public IncorrectSumException(Long sum) {
        super(sum + " is an incorrect amount of money, it should be positive");
    }
}
