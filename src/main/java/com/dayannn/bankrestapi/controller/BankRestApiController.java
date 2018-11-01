package com.dayannn.bankrestapi.controller;


import com.dayannn.bankrestapi.entity.BankAccount;
import com.dayannn.bankrestapi.repository.BankAccountRepository;
import com.dayannn.bankrestapi.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankRestApiController {

   // @Autowired
    private BankAccountRepository bankAccountRepository;


    @PostMapping(value = "/bankaccount/{id}")
    public ResponseEntity<Void> createAccount(@PathVariable Integer id){
        if (id < 0 || id > 99999)
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        if (bankAccountRepository.existsById(id))
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);

        bankAccountRepository.save(new BankAccount(id));

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

  /*  @PutMapping(value = "/bankaccount/{id}/deposit")
    public ResponseEntity<Object> depositIntoAccount(@PathVariable Integer id){
    }

    @PutMapping (value = "/bankaccount/{id}/withdraw")
    public ResponseEntity<Object> withdrawFromAccount(@PathVariable Integer id){
    }

    @GetMapping (value = "/bankaccount/{id}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable Integer id){
    }*/
}
