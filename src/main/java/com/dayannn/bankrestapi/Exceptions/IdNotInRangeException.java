package com.dayannn.bankrestapi.Exceptions;

public class IdNotInRangeException extends RuntimeException {
    public IdNotInRangeException(Integer id) {
        super("Id " + id + "is not in range of allowed ids. Enter id in [1; 99999]");
    }
}
