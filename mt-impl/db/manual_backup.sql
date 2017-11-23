insert into community.account (name, opng_bal, crnt_bal, acct_type, display, CREATED_DATE, MODIFIED_DATE, user_id) select name, opng_bal, crnt_bal, acct_type, display, CREATED_DATE, MODIFIED_DATE, user_id from account where user_id = 4;

insert into community.bnk_stmnt (name, ACCT_ID, DATE_FORMAT, START_ROW, CREATED_DATE, MODIFIED_DATE, user_id) select name, ACCT_ID, DATE_FORMAT, START_ROW, CREATED_DATE, MODIFIED_DATE, user_id from bnk_stmnt where user_id = 4;

insert into community.bnk_stmnt_col_map(BNK_STMNT_ID, PROP_NAME, COL_NAME, DATA_TYPE, CREATED_DATE, MODIFIED_DATE, user_id) select BNK_STMNT_ID, PROP_NAME, COL_NAME, DATA_TYPE, CREATED_DATE, MODIFIED_DATE, user_id  from bnk_stmnt_col_map where user_id = 4;

insert into community.bnk_stmnt_data_field(DATA_MAP_ID,PROP_NAME,DATA,DATA_TYPE, CREATED_DATE, MODIFIED_DATE, user_id) select DATA_MAP_ID,PROP_NAME,DATA,DATA_TYPE, CREATED_DATE, MODIFIED_DATE, user_id  from bnk_stmnt_data_field where user_id = 4;

insert into community.bnk_stmnt_data_map(BNK_STMNT_ID,SEARCH_PROP_NAME,SEARCH_TEXT, CREATED_DATE, MODIFIED_DATE, user_id) select BNK_STMNT_ID,SEARCH_PROP_NAME,SEARCH_TEXT, CREATED_DATE, MODIFIED_DATE, user_id  from bnk_stmnt_data_map where user_id = 4;

insert into community.category(NAME,PRNT_CAT_ID,DFLT_ACCT_ID,TRAN_TYPE,DISPLAY, CREATED_DATE, MODIFIED_DATE, user_id) select NAME,PRNT_CAT_ID,DFLT_ACCT_ID,TRAN_TYPE,DISPLAY, CREATED_DATE, MODIFIED_DATE, user_id  from category where user_id = 4;

insert into community.budget(NAME,CREATED_DATE, MODIFIED_DATE, user_id) select NAME,CREATED_DATE, MODIFIED_DATE, user_id  from budget where user_id = 4;

insert into community.budget_item(BUDGET_ID,CAT_ID,AMOUNT,PERIOD,CREATED_DATE, MODIFIED_DATE, user_id) select BUDGET_ID,CAT_ID,AMOUNT,PERIOD,CREATED_DATE, MODIFIED_DATE, user_id  from budget_item where user_id = 4;

insert into community.setting(NAME,DESCRIPTION,VALUE,TYPE ,CREATED_DATE, MODIFIED_DATE, user_id) select NAME,DESCRIPTION,VALUE,TYPE ,CREATED_DATE, MODIFIED_DATE, user_id  from setting where user_id = 4;

insert into community.transaction(ON_DATE,AMOUNT,CAT_ID,ACCT_ID,DESCRIPTION,CREATED_DATE,MODIFIED_DATE,IS_REC,REC_WHEN,CHECKSUM,FROM_ACCT_ID, user_id) select ON_DATE,AMOUNT,CAT_ID,ACCT_ID,DESCRIPTION,CREATED_DATE,MODIFIED_DATE,IS_REC,REC_WHEN,CHECKSUM,FROM_ACCT_ID,user_id  from transaction where user_id = 4;
 
insert into community.imported_transaction(TRAN_ID,CHECKSUM, user_id) select TRAN_ID,CHECKSUM, user_id  from imported_transaction where user_id = 4;

