-- Inserts for account
insert into account (NAME, OPNG_BAL, CRNT_BAL, ACCT_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Wallet', 0, 0, 'CASH', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('HDFC-BLR', 0, 0, 'BANK', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('ICICI-BLR', 0, 0, 'BANK', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('ICICI-PUN', 0, 0, 'BANK', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into account (NAME, OPNG_BAL, CRNT_BAL, ACCT_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Sodexo', 0, 0, 'CASH', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

-- Inserts for CATEGORY
insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Vehicle', null, (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Fuel', (select id from category p where p.name = 'Vehicle' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Service', (select id from category p where p.name = 'Vehicle' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Parking & Tolls', (select id from category p where p.name = 'Vehicle' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Maintenance', (select id from category p where p.name = 'Vehicle' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Charity', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Debt', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Loan', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Credit Card', (select id from category p where p.name = 'Debt' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Eating Out', (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Entertainment', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Movies', (select id from category p where p.name = 'Entertainment' and user_id = 4), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Games', (select id from category p where p.name = 'Entertainment' and user_id = 4), (select id from account where name = 'Wallet'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Family', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('House', (select id from category p where p.name = 'Family' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Child Care', (select id from category p where p.name = 'Family' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Medical', (select id from category p where p.name = 'Family' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Misc', (select id from category p where p.name = 'Family' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Fitness', (select id from category p where p.name = 'Family' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Home', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Brokerage', (select id from category p where p.name = 'Home' and PRNT_CAT_ID is null and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Deposit', (select id from category p where p.name = 'Home' and PRNT_CAT_ID is null and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Maid', (select id from category p where p.name = 'Home' and PRNT_CAT_ID is null and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Maintenance', (select id from category p where p.name = 'Home' and PRNT_CAT_ID is null and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Rent', (select id from category p where p.name = 'Home' and PRNT_CAT_ID is null and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Household', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Food', (select id from category p where p.name = 'Household' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Milk', (select id from category p where p.name = 'Household' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Grocery', (select id from category p where p.name = 'Household' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Insurance', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Vehicle', (select id from category p where p.name = 'Insurance' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Home', (select id from category p where p.name = 'Insurance' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Life', (select id from category p where p.name = 'Insurance' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Other', (select id from category p where p.name = 'Insurance' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Shopping', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Clothes', (select id from category p where p.name = 'Shopping' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Electronics', (select id from category p where p.name = 'Shopping' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Furniture', (select id from category p where p.name = 'Shopping' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Other', (select id from category p where p.name = 'Shopping' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Footware', (select id from category p where p.name = 'Shopping' and p.prnt_cat_id is null and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Tax', null, (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Income Tax', (select id from category p where p.name = 'Tax'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Property Tax', (select id from category p where p.name = 'Tax'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Zakaath', (select id from category p where p.name = 'Tax'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Other', (select id from category p where p.name = 'Tax'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Travel', null, (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Air Transportation', (select id from category p where p.name = 'Travel'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Ground Transportation', (select id from category p where p.name = 'Travel'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Lodging', (select id from category p where p.name = 'Travel'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Other', (select id from category p where p.name = 'Travel'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Utility', null, (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Cable TV', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Mobile', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Electrical', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Internet', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('LPG', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Newspaper', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Water', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Other', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Trash', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'HDFC-BLR' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));
insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Hair Dressing', (select id from category p where p.name = 'Utility' and user_id = 4), (select id from account where name = 'Wallet' and user_id = 4), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Salary', (select id from account where name = 'HDFC-BLR' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Interest', (select id from account where name = 'HDFC-BLR' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Deposit', (select id from account where name = 'HDFC-BLR' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Tax Refund', (select id from account where name = 'HDFC-BLR' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Other', (select id from account where name = 'HDFC-BLR' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
('Sodexo', (select id from account where name = 'Wallet' and user_id = 4), 'I', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Baby', null, (select id from account where name = 'Wallet'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Medical', (select id from category where name = 'Baby'), (select id from account where name = 'Wallet'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Shopping', (select id from category where name = 'Baby'), (select id from account where name = 'Wallet'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Salma', (select id from category where name = 'Loan'), (select id from account where name = 'HDFC-BLR'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Salma Pocket Money', null, (select id from account where name = 'Wallet'), 'E', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));
-- Inserts for TRANSACTION
insert into transaction (ON_DATE, AMOUNT, CAT_ID, ACCT_ID, DESCRIPTION, CREATED_DATE, MODIFIED_DATE, USER_ID) values 
(now(), 250.00, (select id from category p where p.name = 'Goods'), (select id from account where name = 'Wallet'), 'Fruits', now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into setting (name, description, value, type) values 
('history_report_for', 'No. of Months History Report', '5', 'int'),
('date_format', 'Date Format', 'dd-MM-yyyy h:mm a', 'string');

insert into setting (name, description, value, type) values 
('no_of_top_expenses', 'No. of Top Expenses', '5', 'int'),
('no_of_top_incomes', 'No. of Top Incomes', '3', 'int');

insert into category (NAME, PRNT_CAT_ID, DFLT_ACCT_ID, TRAN_TYPE, DISPLAY, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Transfer', null, (select id from account where name = 'Wallet'), 'T', 1, now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

-- Reporting:
select on_date, sum(amount) from transaction where on_date between '2014-03-01' and '2014-03-31' group by day(on_date);

select month(t.on_date), c.tran_type, a.name, sum(t.amount) from transaction t, category c, account a where t.cat_id = c.id and 
t.acct_id = a.id and date(t.on_date) between '2014-01-01' and '2014-03-31' group by a.name,month(t.on_date),c.tran_type order by t.on_date;

-- Shows the Income & Expense during particular period
select a.name, sum(case when c.tran_type = 'E' then t.amount end) as exp, sum(case when c.tran_type = 'I' then t.amount end) as inc from 
transaction t, category c, account a where t.cat_id = c.id and t.acct_id = a.id and t.on_date between '2014-02-01' and '2014-02-31' 
group by a.name,month(t.on_date) order by t.on_date;

insert into MT_USER (NAME, USERNAME, PASSWORD, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('Maqbool', 'maqbool', 'maqbool', now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('Admin', 'admin', 'admin', now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

insert into MT_USER_ROLE (ROLE_ID, USER_ID, CREATED_DATE, MODIFIED_DATE, USER_ID) values
('U', (select id from MT_USER where name = 'maqbool'), now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('U', (select id from MT_USER where name = 'admin'), now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com')),
('A', (select id from MT_USER where name = 'admin'), now(), now(), (select id from mt_user where username = 'demo.moneytracker@gmail.com'));

