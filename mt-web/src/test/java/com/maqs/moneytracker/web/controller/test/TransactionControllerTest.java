package com.maqs.moneytracker.web.controller.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.dto.TransactionSearchDto;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.services.TransactionService;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionControllerTest extends AbstractContextControllerTest {

  private MockMvc mockMvc;
  
  @Mock
  private TransactionService transactionService;
  
  @Before
  public void setup() throws Exception {
      this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();      
  }
  
  @Test
  public void testListingTransactions() throws Exception {
    PageableList<Transaction> expectedTransactions = getExpectedTransactionsList();
    TransactionSearchDto searchDto = new TransactionSearchDto();
    searchDto.setFromDate(new Date());
    searchDto.setToDate(new Date());
    Mockito.when(
        transactionService.list(searchDto))
        .thenReturn(expectedTransactions); 
    
    Gson gson = new Gson();
    String json = gson.toJson(searchDto);
    
    this.mockMvc.perform(post("/transactions/list").contentType(MediaType.APPLICATION_JSON).content(json))
//    .andExpect(status().isOk())
//    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andDo(print());
  }
  
  private PageableList<Transaction> getExpectedTransactionsList() {
    List<Transaction> transactions = new ArrayList<Transaction>();
    Transaction p = new Transaction();
    p.setDescription("Lunch");
    p.setCategoryId(Long.valueOf(2));
    p.setAccountId(Long.valueOf(2));
    transactions.add(p);
    return new PageableList<Transaction>(transactions, new Page());
  }
}
