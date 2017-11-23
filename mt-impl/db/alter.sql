drop index unq_account_name on account;
alter table account 
add constraint unq_account_name unique (name,user_id);

drop index unq_category_name on category;
alter table category 
add constraint unq_category_name unique (name,prnt_cat_id,tran_type,user_id);

drop index unq_budget_name on budget;
alter table budget 
add constraint unq_budget_name unique (name,user_id);

alter table budget_item drop foreign key fk_category_id;
alter table budget_item drop foreign key fk_budget_id;
drop index unq_budget_cat on budget_item;
alter table budget_item 
add constraint fk_category_id foreign key (cat_id) references category (id ) on delete cascade,
add constraint fk_budget_id foreign key (budget_id) references budget (id ) on delete cascade;
alter table budget_item 
add constraint unq_budget_cat unique (budget_id, cat_id, user_id);

drop index bnk_st_unq_name on bnk_stmnt;
alter table bnk_stmnt 
add constraint bnk_st_unq_name unique (name,user_id);

alter table bnk_stmnt_data_field drop foreign key fk_data_field_data_map_id;
drop index bnk_st_data_field_unq_prop on bnk_stmnt_data_field;
alter table bnk_stmnt_data_field 
add constraint bnk_st_data_field_unq_prop unique (data_map_id, prop_name,user_id),
add constraint fk_data_field_data_map_id foreign key (data_map_id) references bnk_stmnt_data_map (id ) on delete cascade;

alter table mt_user_role drop foreign key fk_userrole_user_id;
drop index unq_userrole_role_user on mt_user_role;
alter table mt_user_role 
add constraint unq_userrole_role_user unique (user_id, role_id),
add constraint fk_userrole_user_id foreign key (user_id) references mt_user (id );

drop index unq_imptran_checksum on imported_transaction;
alter table imported_transaction 
add constraint unq_imptran_checksum unique (checksum,user_id);