ALTER TABLE transaction ADD INDEX `tran_idx_date` (`ON_DATE`);

ALTER TABLE transaction ADD INDEX `tran_idx_account` (`ACCT_ID`);

ALTER TABLE transaction ADD INDEX `tran_idx_cat` (`CAT_ID`);

ALTER TABLE transaction ADD INDEX `tran_idx_amt` (`AMOUNT`);

ALTER TABLE transaction ADD INDEX `tran_idx_rec` (`IS_REC`);

ALTER TABLE ACCOUNT ADD INDEX `acct_idx_name` (`NAME`);

ALTER TABLE ACCOUNT ADD INDEX `acct_idx_accttype` (`ACCT_TYPE`)

ALTER TABLE ACCOUNT ADD INDEX `acct_idx_display` (`DISPLAY`);

ALTER TABLE CATEGORY ADD INDEX `cat_idx_name` (`NAME`);

ALTER TABLE CATEGORY ADD INDEX `cat_idx_trantype` (`TRAN_TYPE`);

ALTER TABLE CATEGORY ADD INDEX `cat_idx_parent_cat` (`PRNT_CAT_ID`);

ALTER TABLE TRANSACTION ADD INDEX `tran_idx_checksum` (`CHECKSUM`);