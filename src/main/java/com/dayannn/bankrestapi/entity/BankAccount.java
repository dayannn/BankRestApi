package com.dayannn.bankrestapi.entity;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    private Integer id;

    @Column(name = "balance")
    private Long balance;

    public BankAccount(Integer id) {
        this(id, Long.valueOf(0));
    }

    public BankAccount(Integer id, Long balance) {
        this.id = id;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public Long getBalance() {return balance;}

    public void setBalance(Long balance) {this.balance = balance;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(id, that.id)
                .append(balance, that.balance)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(id)
                .append(balance)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
