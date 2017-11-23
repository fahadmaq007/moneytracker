package com.maqs.moneytracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ImportedTransaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7786679565872835789L;

	public static final String TRAN_ID = "transactionId";

	public static final String CHECKSUM = "checksum";

	private Long transactionId;
	
	private String checksum;
	
	public ImportedTransaction() {
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

}
