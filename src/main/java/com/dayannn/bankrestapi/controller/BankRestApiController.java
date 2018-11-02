package com.dayannn.bankrestapi.controller;


import com.dayannn.bankrestapi.Exceptions.AccountAlreadyExistsException;
import com.dayannn.bankrestapi.Exceptions.AccountNotFoundException;
import com.dayannn.bankrestapi.Exceptions.IdNotInRangeException;
import com.dayannn.bankrestapi.Exceptions.IncorrectSumException;
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
            //return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            throw new IdNotInRangeException(id);
        if (bankAccountRepository.existsById(id))
            //return new ResponseEntity<Void>(HttpStatus.CONFLICT);
            throw new AccountAlreadyExistsException(id);

        bankAccountRepository.save(new BankAccount(id));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/bankaccount/{id}/deposit", consumes = "application/json")
    public ResponseEntity<Void> depositIntoAccount(@PathVariable Integer id,
                                                   @RequestBody BankAccountOperation oper){
        if (id < 0 || id > 99999)
            //return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            throw new IdNotInRangeException(id);
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);
            //return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        BankAccount account = bankAccountRepository.getOne(id);
        if (oper.getMoney() < 0)
            throw new IncorrectSumException(oper.getMoney());
            //return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);

        account.setBalance(account.getBalance() + oper.getMoney());
        bankAccountRepository.save(account);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping (value = "/bankaccount/{id}/withdraw", consumes = "application/json")
    public ResponseEntity<Void> withdrawFromAccount(@PathVariable Integer id,
                                                    @RequestBody BankAccountOperation oper){
        if (id < 0 || id > 99999)
            //return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            throw new IdNotInRangeException(id);
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);
            //return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        BankAccount account = bankAccountRepository.getOne(id);
        if (oper.getMoney() < 0 || account.getBalance() - oper.getMoney() < 0)
            throw new IncorrectSumException(oper.getMoney());
            //return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);

        account.setBalance(account.getBalance() - oper.getMoney());
        bankAccountRepository.save(account);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping (value = "/bankaccount/{id}/balance")
    public ResponseEntity<BankAccountOperation> getAccountBalance(@PathVariable Integer id){
        if (id < 0 || id > 99999)
           // return new ResponseEntity<BankAccountOperation>(HttpStatus.BAD_REQUEST);
            throw new IdNotInRangeException(id);
        if (!bankAccountRepository.existsById(id))
            throw new AccountNotFoundException(id);
            //return new ResponseEntity<BankAccountOperation>(HttpStatus.NOT_FOUND);

        BankAccount account = bankAccountRepository.getOne(id);
        BankAccountOperation oper = new BankAccountOperation();
        oper.setMoney(account.getBalance());

        return new ResponseEntity<BankAccountOperation>(oper, HttpStatus.OK);
    }
}
