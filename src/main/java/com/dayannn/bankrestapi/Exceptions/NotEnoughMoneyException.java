package com.dayannn.bankrestapi.Exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(Integer id, Long money_amount){
        super("Bank account " + id + " doesn't has " + money_amount + " of money");
    }
}
