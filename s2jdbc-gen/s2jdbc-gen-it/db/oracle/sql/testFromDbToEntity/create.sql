create table department(department_id numeric(8) not null primary key, department_no numeric(2) not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version numeric(8));
create table address(address_id numeric(8) not null primary key, street varchar(20), version numeric(8));
create table employee(employee_id numeric(8) not null primary key, employee_no numeric(4) not null ,employee_name varchar(20),manager_id numeric(8),hiredate date,salary numeric(7,2),department_id numeric(8),address_id numeric(8),version numeric(8), constraint fk_department_id foreign key(department_id) references department(department_id), constraint fk_address_id foreign key(address_id) references address(address_id));
create table comp_key_department(department_id1 numeric(8) not null, department_id2 numeric(8) not null, department_no numeric(2) not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version numeric(8), constraint pk_comp_key_department primary key(department_id1, department_id2));
create table comp_key_address(address_id1 numeric(8) not null, address_id2 numeric(8) not null, street varchar(20), version numeric(8), constraint pk_comp_key_address primary key(address_id1, address_id2));
create table comp_key_employee(employee_id1 numeric(8) not null, employee_id2 numeric(8) not null, employee_no numeric(4) not null ,employee_name varchar(20),manager_id1 numeric(8),manager_id2 numeric(8),hiredate date,salary numeric(7,2),department_id1 numeric(8),department_id2 numeric(8),address_id1 numeric(8),address_id2 numeric(8),version numeric(8), constraint pk_comp_key_employee primary key(employee_id1, employee_id2), constraint fk_comp_key_department_id foreign key(department_id1, department_id2) references comp_key_department(department_id1, department_id2), constraint fk_comp_key_address_id foreign key(address_id1, address_id2) references comp_key_address(address_id1, address_id2));
alter table employee add constraint employee_uk1 unique (address_id);
alter table comp_key_employee add constraint comp_key_employee_uk1 unique (address_id1, address_id2);

create table binary_double_table (binary_double_column binary_double);
create table binary_float_table (binary_float_column binary_float);
create table blob_table (blob_column blob);
create table char_table (char_column char(1));
create table clob_table (clob_column clob);
create table date_table (date_column date);
create table float_table (float_column float);
create table long_table (long_column long);
create table long_raw_table (long_raw_column long raw);
create table nchar_table (nchar_column nchar(1));
create table number_table (number_column number(10,5));
create table nvarchar2_table (nvarchar2_column nvarchar2(2000));
create table raw_table (raw_column raw(2000));
create table timestamp_table (timestamp_column timestamp);
create table varchar2_table (varchar2_column varchar2(4000));

CREATE OR REPLACE FUNCTION FUNC_SIMPLETYPE_PARAM
( param1 IN NUMBER )
RETURN NUMBER
AS
BEGIN
  RETURN 20;
END FUNC_SIMPLETYPE_PARAM;
/