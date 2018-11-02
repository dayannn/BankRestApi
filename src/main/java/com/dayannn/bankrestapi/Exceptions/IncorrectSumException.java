package com.dayannn.bankrestapi.Exceptions;

public class IncorrectSumException extends RuntimeException {
    public IncorrectSumException(Long sum) {
        super(sum + "is not a correct amount of money, it should be positive");
    }
}
