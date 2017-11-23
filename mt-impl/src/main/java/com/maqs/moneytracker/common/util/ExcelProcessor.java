package com.maqs.moneytracker.common.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.model.BankStatement;
import com.maqs.moneytracker.model.ColumnMap;
import com.maqs.moneytracker.model.Transaction;

public class ExcelProcessor {

	private static final List<String> excelColumns = new ArrayList<String>();

	public static final String EXCEL_EXTN = "xls";

	public static final String EXCELX_EXTN = "xlsx";
	
	private static final String splChrs = "[-/@*#$%^&_+=()]+";

	private static final String stringChrs = ".*[a-zA-Z]+.*";

	private static final String DATE = "date";

	private static final String DECIMAL = "decimal";
	
	private static final String TEXT = "TEXT";
	
	static {
		for (int i = 65; i < 75; i++) {
			char c = (char) i;
			excelColumns.add("" + c);
		}
		System.out.println("excelColumns: " + excelColumns);
	}

	public static void main(String[] args) {
		/*System.out.println("**STATEMENT SUMMARY****".matches(splChrs));
		System.out.println("NEFT Dr--BARB0SHIMOG-Abbu-NETBANK, MUM-07".matches(splChrs));*/
		String userHome = System.getProperty("user.home");
		if (userHome == null) {
			userHome = File.separator;
		}
		System.out.println(userHome + File.separator);
	
		StringBuilder b = new StringBuilder();
		b.append("ATM Withdrawl").append(BigDecimal.valueOf(10000.00)).append(new Date());
		String md5 = DigestUtils.md5Hex(b.toString());
		System.out.println(md5);
		StringBuilder b1 = new StringBuilder();
		b1.append("ATM Withdrawl").append(BigDecimal.valueOf(10000.00)).append(new Date());
		md5 = DigestUtils.md5Hex(b1.toString());
		System.out.println(md5);
		/*File f1 = new File("E:/personal/Bank Statements/2012-13/HDFC-01Aug12-10Jul13 - Copy.xls");
		File f2 = new File("E:/personal/Bank Statements/2012-13/HDFC-01Aug12-10Jul13.xls");
		try {
			System.out.println(f1.getAbsolutePath() + " " + f1.exists());
			System.out.println(f2.getAbsolutePath() + " " + f2.exists());
			System.out.println("FileUtils.contentEquals(f1, f2) " + FileUtils.contentEquals(f1, f2));
			String hex1 = DigestUtils.md5Hex(FileUtils.readFileToByteArray(f1));
			System.out.println(hex1);
			String hex2 = DigestUtils.md5Hex(FileUtils.readFileToByteArray(f2));
			System.out.println(hex2);
			if (hex1.equals(hex2)) {
				System.out.println("both are equal");
			} else {
				System.out.println("not equal");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static List<String> getExcelColumns() {
		return excelColumns;
	}
	
	public static List getRowObjects(BankStatement bankStatement,
			Workbook workbook, Class clazz) throws ServiceException {
		List rowObjects = new ArrayList();
		try {
			int numberOfSheets = workbook.getNumberOfSheets();

			int start = bankStatement.getStartRow();
			int end = bankStatement.getEndRow();

			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if (end == 0) {
					end = sheet.getLastRowNum();
				}
				
				for (int rowIndex = start; rowIndex < end; rowIndex++) {
					// Get the row object
					Row row = sheet.getRow(rowIndex);
					Object rowObject = clazz.newInstance();
					AppUtil.setFieldValue(rowObject, Transaction.ON_DATE, null);
					List<ColumnMap> columnMaps = bankStatement.getColumnMaps();
					columnloop:
					for (ColumnMap columnMap : columnMaps) {
						String columnName = columnMap.getColumnName();
						if (StringUtil.nullOrEmpty(columnName)) {
							continue;
						}
						String propertyName = columnMap.getPropertyName();
						if (StringUtil.nullOrEmpty(columnName)) {
							continue;
						}
						if (row == null) {
							break columnloop;
						}
						Cell cell = row.getCell(excelColumns
								.indexOf(columnName));
						if (cell == null) {
							continue;
						}
						String value = cell.toString();
						if (! StringUtil.nullOrEmpty(value)) {
							boolean hasOnlySpecialChars = value.matches(splChrs);
							if (hasOnlySpecialChars) {
								break columnloop;
							} else {
								value = value.trim();
								Object o = null;
								String dataType = columnMap.getDataType();
								if (dataType.equalsIgnoreCase(DATE)) {
									String format = bankStatement.getDateFormat();
									try {
										o = DateUtil.getDate(value, format);
										AppUtil.setFieldValue(rowObject, Transaction.DATE_STRING, value);
									} catch (ParseException e) {
										break columnloop;
									}
								} else if (dataType.equalsIgnoreCase(DECIMAL)) {
									if (value.matches(stringChrs)) {
										break columnloop;
									}
									try {
										o = AppUtil.getAmount(value);
									} catch (ParseException e){
										o = BigDecimal.ZERO;
									}
								} else if (dataType.equalsIgnoreCase(TEXT)){
									o = value;
								}
								if (o != null) {
									AppUtil.setFieldValue(rowObject, propertyName, o);
								}
							} 
						}
					}
					if (AppUtil.getFieldValue(rowObject, Transaction.ON_DATE) != null) {
						rowObjects.add(rowObject);
					}
				} // end of rows iterator
			} // end of sheets for loop
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return rowObjects;
	}

}
