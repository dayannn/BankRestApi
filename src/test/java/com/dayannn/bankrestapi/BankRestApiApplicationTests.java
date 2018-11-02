package com.dayannn.bankrestapi;

import com.dayannn.bankrestapi.Exceptions.*;
import com.dayannn.bankrestapi.controller.BankRestApiController;
import com.dayannn.bankrestapi.entity.BankAccount;
import com.dayannn.bankrestapi.repository.BankAccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BankRestApiApplicationTests {

    private MockMvc mockMvc;

    private BankAccount acc1 = new BankAccount(2055);
    private BankAccount acc2 = new BankAccount(2056, Long.valueOf(100));
    private int idToBeCreated = 195;
    private int idThatDoesNotExist = 3000;
    private int idThatIsNotInRange = -10;

    @Autowired
    BankRestApiController controller;

    @Autowired
    private BankAccountRepository repo;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        repo.deleteAll();
        repo.save(acc1);
        repo.save(acc2);
    }

    // createAccount() tests

    @Test (expected = IdNotInRangeException.class)
    public void accountCreateTest_badId() throws Throwable{
        try {
            mockMvc.perform(post("/bankaccount/{id}", idThatIsNotInRange)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isBadRequest());
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof IdNotInRangeException);
            throw e.getCause();
        }
    }

    @Test (expected = AccountAlreadyExistsException.class)
    public void accountCreateTest_exists() throws Throwable{
        try {
            mockMvc.perform(post("/bankaccount/{id}", acc1.getId())
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isConflict());
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof AccountAlreadyExistsException);
            throw e.getCause();
        }
    }

    @Test
    public void accountCreateTest_OK() throws Throwable{
        mockMvc.perform(post("/bankaccount/{id}", idToBeCreated)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }


    // depositIntoAccountTests

    @Test (expected = AccountNotFoundException.class)
    public void depositIntoAccountTest_notExist() throws Throwable{
        try {
            mockMvc.perform(put("/bankaccount/{id}/deposit", idThatDoesNotExist)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"money\":10}"))
                    .andExpect(status().isNotFound());
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof AccountNotFoundException);
            throw e.getCause();
        }
    }

    @Test (expected = IncorrectSumException.class)
    public void depositIntoAccountTest_incorrectSum() throws Throwable{
        try {
            mockMvc.perform(put("/bankaccount/{id}/deposit", acc1.getId())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"money\":-5}"));
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof IncorrectSumException);
            throw e.getCause();
        }
    }

    @Test
    public void depositIntoAccountTest_OK() throws Throwable{
        mockMvc.perform(put("/bankaccount/{id}/deposit", acc1.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"money\":10}"))
                .andExpect(status().isOk());
    }

    // withdrawFromAccount tests

    @Test (expected = AccountNotFoundException.class)
    public void withdrawFromAccountTest_notExist() throws Throwable{
        try {
            mockMvc.perform(put("/bankaccount/{id}/withdraw", idThatDoesNotExist)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"money\":10}"))
                    .andExpect(status().isNotFound());
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof AccountNotFoundException);
            throw e.getCause();
        }
    }

    @Test (expected = IncorrectSumException.class)
    public void wthdrawFromAccountTest_incorrectSum() throws Throwable{
        try {
            mockMvc.perform(put("/bankaccount/{id}/withdraw", acc2.getId())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"money\":-5}"));
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof IncorrectSumException);
            throw e.getCause();
        }
    }

    @Test (expected = NotEnoughMoneyException.class)
    public void wthdrawFromAccountTest_notEnoughMoney() throws Throwable{
        try {
            mockMvc.perform(put("/bankaccount/{id}/withdraw", acc2.getId())
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content("{\"money\":200}"));
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof NotEnoughMoneyException);
            throw e.getCause();
        }
    }

    @Test
    public void withdrawFromAccountTest_OK() throws Throwable{
        mockMvc.perform(put("/bankaccount/{id}/withdraw", acc2.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"money\":10}"))
                .andExpect(status().isOk());
    }

    // getAccountBalance tests

    @Test
    public void getAccountBalanceTest_OK() throws Exception{
        mockMvc.perform(get("/bankaccount/{id}/balance", acc2.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.money").value(acc2.getBalance()));
    }

    @Test (expected = AccountNotFoundException.class)
    public void getAccountBalanceTest_wrongAccount() throws Throwable{
        try {
            mockMvc.perform(get("/bankaccount/{id}/balance", idThatDoesNotExist))
                    .andExpect(status().isNotFound());
        }
        catch (NestedServletException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertTrue(e.getCause() instanceof AccountNotFoundException);
            throw e.getCause();
        }
    }


}
