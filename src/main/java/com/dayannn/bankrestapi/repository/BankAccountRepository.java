package com.dayannn.bankrestapi.repository;

import com.dayannn.bankrestapi.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer>{ }
