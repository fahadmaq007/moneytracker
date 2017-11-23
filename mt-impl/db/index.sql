ALTER TABLE account ADD INDEX `account_idx_userid` (`USER_ID`);

ALTER TABLE bnk_stmnt ADD INDEX `bnk_stmnt_idx_userid` (`USER_ID`);

ALTER TABLE bnk_stmnt_col_map ADD INDEX `bnk_stmnt_col_map_idx_userid` (`USER_ID`);

ALTER TABLE bnk_stmnt_data_field ADD INDEX `bnk_stmnt_data_field_idx_userid` (`USER_ID`);

ALTER TABLE bnk_stmnt_data_map ADD INDEX `bnk_stmnt_data_map_idx_userid` (`USER_ID`);

ALTER TABLE budget ADD INDEX `budget_idx_userid` (`USER_ID`);

ALTER TABLE budget_item ADD INDEX `budget_item_idx_userid` (`USER_ID`);

ALTER TABLE future_transaction ADD INDEX `future_transaction_idx_userid` (`USER_ID`);

ALTER TABLE setting ADD INDEX `setting_idx_userid` (`USER_ID`);

ALTER TABLE transaction ADD INDEX `transaction_idx_userid` (`USER_ID`);

ALTER TABLE user_def_cat ADD INDEX `user_def_cat_idx_userid` (`USER_ID`);

