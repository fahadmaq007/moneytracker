package com.maqs.moneytracker.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.AppUtil;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.DateUtil;
import com.maqs.moneytracker.common.util.ExcelProcessor;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.common.util.Util;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.Budget;
import com.maqs.moneytracker.model.ColumnMap;
import com.maqs.moneytracker.model.DataField;
import com.maqs.moneytracker.model.DataMap;
import com.maqs.moneytracker.model.ImportedTransaction;
import com.maqs.moneytracker.model.Transaction;
import com.maqs.moneytracker.security.LoggedInChecker;
import com.maqs.moneytracker.server.core.dao.IDao;
import com.maqs.moneytracker.server.core.exception.DataAccessException;
import com.maqs.moneytracker.types.MessageType;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class ImportServiceImpl implements ImportService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDao dao;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private LoggedInChecker loggedInChecker;

	public ImportServiceImpl() {

	}

	public ImportServiceImpl(IDao dao) {
		setDao(dao);
	}

	public void setDao(IDao categoryDao) {
		this.dao = categoryDao;
	}

	public IDao getDao() {
		return dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BankStatement> listBankStatements(Page page)
			throws ServiceException {
		List<BankStatement> statements = null;
		try {
			logger.debug("listAll() is been called");
			QuerySpec querySpec = loggedInChecker
					.getQuerySpec(BankStatement.class);
			statements = (List<BankStatement>) dao.listAll(querySpec, page);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return statements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public BankStatement storeBankStatement(BankStatement s)
			throws ServiceException {
		try {
			logger.debug("store() is been called");
			int actionIndex = s.getAction().getActionIndex();
			if (Action.CREATE_NEW == actionIndex) {
				Long userId = loggedInChecker.getCurrentUserId();
				s.setUserId(userId);
			}
			s = (BankStatement) dao.saveOrUpdate(s);
			storeColumnMaps(s);
			storeDataMaps(s);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return s;
	}

	private void storeColumnMaps(BankStatement s) throws ServiceException {
		List<ColumnMap> columnMaps = s.getColumnMaps();
		if (CollectionsUtil.isNonEmpty(columnMaps)) {
			Long userId = loggedInChecker.getCurrentUserId();
			for (ColumnMap columnMap : columnMaps) {
				int actionIndex = columnMap.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					columnMap.setUserId(userId);
				}
				columnMap.setBankStatementId(s.getId());
			}
			try {
				logger.debug("storeColumnMaps() is been called");
				dao.saveAll(columnMaps);
			} catch (DataAccessException e) {
				throw new ServiceException(e);
			}
		}
	}

	private void storeDataMaps(BankStatement s) throws ServiceException {
		List<DataMap> dataMaps = s.getDataMaps();
		Long userId = loggedInChecker.getCurrentUserId();
		if (CollectionsUtil.isNonEmpty(dataMaps)) {
			for (DataMap dataMap : dataMaps) {
				int actionIndex = dataMap.getAction().getActionIndex();
				if (Action.CREATE_NEW == actionIndex) {
					dataMap.setUserId(userId);
				}
				dataMap.setBankStatementId(s.getId());
			}
			try {
				logger.debug("storeColumnMaps() is been called");
				dao.saveAll(dataMaps);
				List<DataField> dataFields = new ArrayList<DataField>();
				for (DataMap dataMap : dataMaps) {
					List<DataField> fields = dataMap.getDataFields();
					if (CollectionsUtil.isNonEmpty(fields)) {
						for (DataField dataField : fields) {
							int actionIndex = dataField.getAction()
									.getActionIndex();
							if (Action.CREATE_NEW == actionIndex) {
								dataField.setUserId(userId);
							}
							dataField.setDataMapId(dataMap.getId());
						}
						dataFields.addAll(fields);
					}
				}
				if (CollectionsUtil.isNonEmpty(dataFields)) {
					dao.saveAll(dataFields);
				}
			} catch (DataAccessException e) {
				throw new ServiceException(e);
			}
		}
	}

	@Override
	public List<Transaction> importBankStatement(Long bankStatementId,
			MultipartFile file, int startRow, int endRow, String dateFormat)
			throws ServiceException {
		List<Transaction> transactions = null;

		BankStatement bankStatement = getBankStatement(bankStatementId);
		if (bankStatement == null) {
			throw new ServiceException("no bank statement available with id: "
					+ bankStatementId);
		}
		if (startRow > 0) {
			bankStatement.setStartRow(startRow);
		}

		if (!StringUtil.nullOrEmpty(dateFormat)) {
			bankStatement.setDateFormat(dateFormat);
		}

		if (endRow > 0) {
			bankStatement.setEndRow(endRow);
		}
		File f = store(file);
		if (f == null) {
			return null;
		}
		try {
			String fileName = f.getName();
			List rowObjects = null;
			if (fileName.toLowerCase().endsWith(ExcelProcessor.EXCEL_EXTN)
					|| fileName.toLowerCase().endsWith(
							ExcelProcessor.EXCELX_EXTN)) {
				Workbook workbook = getWorkbook(f);
				rowObjects = ExcelProcessor.getRowObjects(bankStatement,
						workbook, Transaction.class);
			} else {
				throw new ServiceException(
						"other than excel sheet is not supported at the moment");
			}

			transactions = processRawObjects(bankStatement, rowObjects);

		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		} finally {
			if (f.exists()) {
				logger.debug("deleting file: " + f.getAbsolutePath());
				f.delete();
			}
		}
		return transactions;
	}

	private List<Transaction> processRawObjects(BankStatement bankStatement,
			List rowObjects) throws ServiceException {
		List<Transaction> transactions = null;
		if (CollectionsUtil.isNonEmpty(rowObjects)) {
			transactions = new ArrayList<Transaction>();
			for (Object object : rowObjects) {
				Transaction t = (Transaction) object;
				if (t.getOnDate() != null) {
					transactions.add(t);
				}
			}

			Set<String> checksumList = new HashSet<String>();
			for (Transaction t : transactions) {
				String checksum = transactionService.getChecksum(t);
				checksumList.add(checksum);
				t.setOriginalChecksum(checksum);
			}

			List<ImportedTransaction> importedTransactions = listByChecksum(checksumList);
			Set<String> duplicateChecksumList = getChecksumList(importedTransactions);
			for (Transaction t : transactions) {
				String checksum = t.getOriginalChecksum();
				if (duplicateChecksumList.contains(checksum)) {
					t.setMessage(Constants.MSG_POTENTIAL_DUPLICATE_TRANSACTION);
					t.setMessageType(MessageType.TYPE_WARN);
				} else {
					t.setMessage(Constants.MSG_OK_TO_SAVE);
					t.setMessageType(MessageType.TYPE_SUCCESS);
				}
			}

			try {
				applyDataMap(bankStatement, transactions);
			} catch (Exception e) {
				throw new ServiceException(e.getMessage(), e);
			}

			transactionService.fetchCategories(transactions);
			transactionService.fetchAccounts(transactions);
		}
		return transactions;
	}

	private Set<String> getChecksumList(
			List<ImportedTransaction> importedTransactions) {
		Set<String> checksumList = new HashSet<String>();
		for (ImportedTransaction t : importedTransactions) {
			checksumList.add(t.getChecksum());
		}
		return checksumList;
	}

	private List<ImportedTransaction> listByChecksum(Set<String> checksumList)
			throws ServiceException {
		if (!CollectionsUtil.isNonEmpty(checksumList)) {
			return null;
		}
		QuerySpec querySpec = loggedInChecker
				.getQuerySpec(ImportedTransaction.class);
		querySpec.addPropertySpec(new PropertySpec(
				ImportedTransaction.CHECKSUM, Operation.IN, checksumList));
		List<ImportedTransaction> list = null;
		try {
			list = (List<ImportedTransaction>) dao.listAll(querySpec);
		} catch (DataAccessException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return list;
	}

	private void applyDataMap(BankStatement bankStatement,
			List<Transaction> transactions) throws Exception {
		if (CollectionsUtil.isNonEmpty(transactions)) {
			for (Transaction t : transactions) {
				for (DataMap dataMap : bankStatement.getDataMaps()) {
					String searchField = dataMap.getSearchField();
					String searchText = dataMap.getSearchText();
					Object value = AppUtil.getFieldValue(t, searchField);
					if (value instanceof String) {
						String val = (String) value;
						if (val.toLowerCase()
								.contains(searchText.toLowerCase())) {
							updateFieldValue(bankStatement, t, dataMap);
						}
					}
				}
			}
		}
	}

	private void updateFieldValue(BankStatement bankStatement, Transaction t,
			DataMap dataMap) throws Exception {
		List<DataField> dataFields = dataMap.getDataFields();
		for (DataField dataField : dataFields) {
			String dataAsString = dataField.getDataAsString();
			String propName = dataField.getPropertyName();
			String dataType = dataField.getDataType();
			if (dataType.equalsIgnoreCase("date")) {
				Date d = DateUtil.getDate(dataAsString,
						bankStatement.getDateFormat());
				AppUtil.setFieldValue(t, propName, d);
			} else if (dataType.equalsIgnoreCase("decimal")) {
				BigDecimal dec = (BigDecimal) AppUtil.getAmount(dataAsString);
				AppUtil.setFieldValue(t, propName, dec);
			} else if (dataType.equalsIgnoreCase("long")) {
				Long l = Long.valueOf(dataAsString);
				AppUtil.setFieldValue(t, propName, l);
			} else if (dataType.equalsIgnoreCase("text")) {
				AppUtil.setFieldValue(t, propName, dataAsString);
			}
		}
	}

	private File store(MultipartFile file) throws ServiceException {
		File f = null;
		if (!file.isEmpty()) {
			BufferedOutputStream buffStream = null;
			try {
				String fileName = null;
				fileName = file.getOriginalFilename();
				byte[] bytes = file.getBytes();
				String tmpdir = Util.getTempDirectory();
				f = new File(tmpdir + File.separator + fileName);
				buffStream = new BufferedOutputStream(new FileOutputStream(f));
				buffStream.write(bytes);
				logger.debug("file is uploaded at " + f.getAbsolutePath());
			} catch (Exception e) {
				throw new ServiceException(e.getMessage(), e);
			} finally {
				if (buffStream != null) {
					try {
						buffStream.close();
					} catch (IOException e) {
						throw new ServiceException(e.getMessage(), e);
					}
				}
			}
		} else {
			throw new IllegalArgumentException("file is empty");
		}
		return f;
	}

	private Workbook getWorkbook(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("given file is null");
		}
		// Create Workbook instance for xlsx/xls file input stream
		Workbook workbook = null;
		String fileName = file.getName();
		FileInputStream stream = new FileInputStream(file);
		if (fileName.toLowerCase().endsWith("xlsx")) {
			workbook = new XSSFWorkbook(stream);
		} else if (fileName.toLowerCase().endsWith("xls")) {
			workbook = new HSSFWorkbook(stream);
		}

		return workbook;
	}

	public List<String> getExcelColumns() {
		return ExcelProcessor.getExcelColumns();
	}

	@Override
	public BankStatement getBankStatement(Long id) throws ServiceException {
		BankStatement s = null;
		try {
			logger.debug("getBankStatement() is been called");
			s = (BankStatement) dao.getEntity(BankStatement.class, id);
			loadBankStatementDetails(s);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return s;
	}

	@Override
	public BankStatement getBankStatementDetails(BankStatement s)
			throws ServiceException {
		logger.debug("getBankStatementDetails() is been called");
		if (s == null) {
			throw new IllegalArgumentException("given statement is null");
		}
		Long id = s.getId();
		if (id == null) {
			throw new IllegalArgumentException("given statement's id is null");
		}
		try {
			s = (BankStatement) dao.getEntity(BankStatement.class, id);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		loadBankStatementDetails(s);
		return s;
	}

	private void loadBankStatementDetails(BankStatement s)
			throws ServiceException {
		if (s != null) {
			try {
				Long id = s.getId();
				QuerySpec columnMapSpec = loggedInChecker
						.getQuerySpec(ColumnMap.class);
				columnMapSpec.addPropertySpec(new PropertySpec(
						ColumnMap.BANK_ST_ID, id));

				List<ColumnMap> columnMaps = (List<ColumnMap>) dao
						.listAll(columnMapSpec);
				s.setColumnMaps(columnMaps);

				QuerySpec dataMapSpec = loggedInChecker
						.getQuerySpec(DataMap.class);
				dataMapSpec.addPropertySpec(new PropertySpec(
						ColumnMap.BANK_ST_ID, id));
				List<DataMap> dataMaps = (List<DataMap>) dao
						.listAll(dataMapSpec);
				s.setDataMaps(dataMaps);

				if (CollectionsUtil.isNonEmpty(dataMaps)) {
					Set<Long> dataMapIds = AppUtil.getIds(dataMaps);
					Map<Long, List<? extends Entity>> map = dao
							.listChildrenByParentIds(DataField.class,
									DataField.DATA_MAP_ID, dataMapIds);
					for (DataMap dataMap : dataMaps) {
						Long dataId = dataMap.getId();
						List<DataField> dataFields = (List<DataField>) map
								.get(dataId);
						dataMap.setDataFields(dataFields);
					}
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public List<ImportedTransaction> storeImportedTransactions(
			List<Transaction> transactions) throws ServiceException {
		List<ImportedTransaction> importedTransactions = null;
		if (CollectionsUtil.isNonEmpty(transactions)) {
			importedTransactions = new ArrayList<ImportedTransaction>(
					transactions.size());
			Long userId = loggedInChecker.getCurrentUserId();
			Set<String> checksumList = new HashSet<String>();
			for (Transaction t : transactions) {
				String checksum = t.getOriginalChecksum();
				checksumList.add(checksum);
			}
			try {
				List<ImportedTransaction> alreadyImportedTransactions = listByChecksum(checksumList);				
				Map<String, ImportedTransaction> map = getImportedTransactionMapByChecksum(alreadyImportedTransactions);
				for (Transaction t : transactions) {
					String checksum = t.getOriginalChecksum();
					if (! map.containsKey(checksum)) {
						ImportedTransaction i = new ImportedTransaction();
						i.setTransactionId(t.getId());
						i.setChecksum(checksum);
						checksumList.add(checksum);
						importedTransactions.add(i);
						i.setUserId(userId);
					} 						
				}
				if (CollectionsUtil.isNonEmpty(importedTransactions)) {
					dao.saveAll(importedTransactions);
				}
			} catch (DataAccessException e) {
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return importedTransactions;
	}

	private Map<String, ImportedTransaction> getImportedTransactionMapByChecksum(
			List<ImportedTransaction> importedTransactions) {
		Map<String, ImportedTransaction> map = new HashMap<String, ImportedTransaction>();
		if (CollectionsUtil.isNonEmpty(importedTransactions)) {
			for (ImportedTransaction importedTransaction : importedTransactions) {
				map.put(importedTransaction.getChecksum(), importedTransaction);
			}
		}
			
		return map;
	}

	@Override
	public long totalStatements() throws ServiceException {
		long count = 0;
		try {
			QuerySpec querySpec = loggedInChecker.getQuerySpec(BankStatement.class);
			count = dao.count(querySpec);	
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		return count;
	}
}
