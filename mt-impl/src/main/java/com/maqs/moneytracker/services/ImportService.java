package com.maqs.moneytracker.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.ImportedTransaction;
import com.maqs.moneytracker.model.Transaction;

public interface ImportService {

	public List<BankStatement> listBankStatements(Page page)
			throws ServiceException;
	
	public BankStatement storeBankStatement(BankStatement s) throws ServiceException;
	
	public BankStatement getBankStatement(Long id) throws ServiceException;
	
	public BankStatement getBankStatementDetails(BankStatement s) throws ServiceException;
	
	public List<Transaction> importBankStatement(Long bankStatementId, MultipartFile file,
			int startRow, int endRow, String dateFormat) throws ServiceException;
	
	public List<String> getExcelColumns() throws ServiceException;

	public List<ImportedTransaction> storeImportedTransactions(List<Transaction> transactions) throws ServiceException;
	
	public long totalStatements() throws ServiceException;
}
