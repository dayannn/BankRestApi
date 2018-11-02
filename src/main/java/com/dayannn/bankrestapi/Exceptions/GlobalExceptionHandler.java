package com.dayannn.bankrestapi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(AccountNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String accountAlreadyExistsHandler(AccountAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IdNotInRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String idNotInRangeHandler(IdNotInRangeException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectSumException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String incorrectSumHandler(IncorrectSumException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotEnoughMoneyException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String notEnoughMoneyHandler(NotEnoughMoneyException ex) {
        return ex.getMessage();
    }
}
