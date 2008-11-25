create table department(department_id int not null primary key, department_no int not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version int);
create table address(address_id int not null primary key, street varchar(20), version int);
create table employee(employee_id int not null primary key, employee_no int not null ,employee_name varchar(20),manager_id int,hiredate datetime,salary decimal(7,2),department_id int,address_id int, version int, constraint fk_department_id foreign key(department_id) references department(department_id), constraint fk_address_id foreign key(address_id) references address(address_id));
create table comp_key_department(department_id1 int not null, department_id2 int not null, department_no int not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version int, constraint pk_comp_key_department primary key(department_id1, department_id2));
create table comp_key_address(address_id1 int not null, address_id2 int not null, street varchar(20), version int, constraint pk_comp_key_address primary key(address_id1, address_id2));
create table comp_key_employee(employee_id1 int not null, employee_id2 int not null, employee_no int not null ,employee_name varchar(20),manager_id1 int,manager_id2 int,hiredate datetime,salary decimal(7,2),department_id1 int,department_id2 int,address_id1 int,address_id2 int,version int, constraint pk_comp_key_employee primary key(employee_id1, employee_id2), constraint fk_comp_key_department_id foreign key(department_id1, department_id2) references comp_key_department(department_id1, department_id2), constraint fk_comp_key_address_id foreign key(address_id1, address_id2) references comp_key_address(address_id1, address_id2));
alter table employee add constraint employee_uk1 unique (address_id);
alter table comp_key_employee add constraint comp_key_employee_uk1 unique (address_id1, address_id2);

create table bigint_table (bigint_column bigint);
create table int_table (int_column int);
create table smallint_table (smallint_column smallint);
create table tinyint_table (tinyint_column tinyint);
create table bit_table (bit_column bit);
create table decimal_table (decimal_column decimal);
create table numeric_table (numeric_column numeric);
create table money_table (money_column money);
create table smallmoney_table (smallmoney_column smallmoney);
create table float_table (float_column float);
create table real_table (real_column real);
create table datetime_table (datetime_column datetime);
create table smalldatetime_table (smalldatetime_column smalldatetime);
create table char_table (char_column char);
create table varchar_table (varchar_column varchar);
create table varchar_max_table (varchar_max_column varchar(max));
create table text_table (text_column text);
create table nchar_table (nchar_nchar nchar);
create table nvarchar_table (nvarchar_column nvarchar);
create table nvarchar_max_table (nvarchar_max_column nvarchar(max));
create table ntext_table (ntext_column ntext);
create table binary_table (binary_column binary);
create table varbinary_table (varbinary_column varbinary);
create table varbinary_max_table (varbinary_max_column varbinary(max));
create table image_table (image_column image);
create table テーブル (カラム varchar(255));

CREATE FUNCTION [dbo].[FUNC_SIMPLETYPE_PARAM] (
    @param1 int)
RETURNS int
AS
BEGIN
    RETURN 20;
END
GO
