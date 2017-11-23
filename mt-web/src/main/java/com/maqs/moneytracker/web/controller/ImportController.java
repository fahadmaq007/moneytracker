package com.maqs.moneytracker.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.ImportedTransaction;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.services.ImportService;
import com.wordnik.swagger.annotations.Api;

/**
 * The REST webservices are exposed to the external world.
 * 
 * @author maqbool.ahmed
 */
@Controller
@RequestMapping("/api/import")
@Api(value = "import", description = "Import API")
@Secured("ROLE_USER")
public class ImportController {

	@Autowired
	private ImportService importService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final Page defaultPage = new Page(1, 10);

	/**
	 * Lists the Categories by given search criteria.
	 * 
	 * @param page
	 *            current page
	 * @return List of Project Dtos
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	List<BankStatement> listBankStatements() throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listBankStatements method is been called");

		List<BankStatement> statements = importService.listBankStatements(defaultPage);
		if (logger.isInfoEnabled())
			logger.info("listBankStatements() has listed "
					+ (statements == null ? 0 : statements.size()) + " records");

		return statements;
	}

	/**
	 * Lists the Excel Columns.
	 * 
	 * @return List of Column Names
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/excelcolumns", method = RequestMethod.GET)
	public @ResponseBody
	List<String> listExcelColumns() throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("listExcelColumns method is been called");

		List<String> columns = importService.getExcelColumns();
		return columns;
	}
	
	@RequestMapping(value = "/storestatement", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody
	BankStatement storeBankStatement(@RequestBody BankStatement bankStatement)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("storeBankStatement method is been called");

		BankStatement statement = importService.storeBankStatement(bankStatement);
		return statement;
	}

	@RequestMapping(value = "/importbankst", method = RequestMethod.POST)
	public @ResponseBody
	List<Transaction> importBankStatement(@RequestParam(value="bankStatementId", required = true) Long bankStatementId,
			@RequestParam(value="file", required = true) MultipartFile file,
			@RequestParam("startRow") int startRow, 
			@RequestParam("endRow") int endRow,
			@RequestParam("dateFormat") String dateFormat) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("importBankStatement method is been called");
		return importService.importBankStatement(bankStatementId, file, startRow, endRow, dateFormat);
	}
	
	@RequestMapping(value = "/bankstatementdetails", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	BankStatement getBankStatementDetails(@RequestBody BankStatement bankStatement) throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("getBankStatementDetails method is been called");
		return importService.getBankStatementDetails(bankStatement);
	}
	
	/**
	 * Stores the transactions.
	 * 
	 * @param transactions
	 *            transactions to be stored.
	 * @return Transaction
	 * @throws ServiceException
	 */
	@RequestMapping(value = "storeimportedtransactions", method = RequestMethod.PUT)
	public @ResponseBody
	List<ImportedTransaction> storeImportedTransactions(@RequestBody List<Transaction> transactions)
			throws ServiceException {
		if (logger.isDebugEnabled())
			logger.debug("importTransactions method is been called");
		return importService.storeImportedTransactions(transactions);
	}
}
