package com.dayannn.bankrestapi.controller;


import com.dayannn.bankrestapi.Exceptions.*;
import com.dayannn.bankrestapi.entity.BankAccount;
import com.dayannn.bankrestapi.entity.BankAccountOperation;
import com.dayannn.bankrestapi.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankRestApiController {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostMapping(value = "/bankaccount/{id}")
    public ResponseEntity<Void> createAccount(@PathVariable Integer id){
        if (id < 0 || id > 99999)
            throw new IdNotInRangeException(id);
        if (bankAccountRepository.existsById(id))
            throw new AccountAlreadyExistsException(id);

        bankAccountRepository.save(new BankAccount(id));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/bankaccount/{id}/deposit", consumes = "application/json")
    public ResponseEntity<Void> depositIntoAccount(@PathVariable Integer id,
                                                   @RequestBody BankAccountOperation oper){
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);

        BankAccount account = bankAccountRepository.getOne(id);
        if (oper.getMoney() < 0)
            throw new IncorrectSumException(oper.getMoney());

        account.setBalance(account.getBalance() + oper.getMoney());
        bankAccountRepository.save(account);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping (value = "/bankaccount/{id}/withdraw", consumes = "application/json")
    public ResponseEntity<Void> withdrawFromAccount(@PathVariable Integer id,
                                                    @RequestBody BankAccountOperation oper){
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);

        BankAccount account = bankAccountRepository.getOne(id);
        if (oper.getMoney() < 0)
            throw new IncorrectSumException(oper.getMoney());

        if (account.getBalance() - oper.getMoney() < 0)
            throw new NotEnoughMoneyException(id, oper.getMoney());

        account.setBalance(account.getBalance() - oper.getMoney());
        bankAccountRepository.save(account);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping (value = "/bankaccount/{id}/balance")
    public ResponseEntity<BankAccountOperation> getAccountBalance(@PathVariable Integer id){
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);

        BankAccount account = bankAccountRepository.getOne(id);
        BankAccountOperation oper = new BankAccountOperation();
        oper.setMoney(account.getBalance());

        return new ResponseEntity<BankAccountOperation>(oper, HttpStatus.OK);
    }
}
