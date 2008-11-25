create table department(department_id integer not null primary key, department_no numeric(2) not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer);
create table address(address_id integer not null primary key, street varchar(20), version integer);
create table employee(employee_id integer not null primary key, employee_no numeric(4) not null ,employee_name varchar(20),manager_id integer,hiredate date,salary numeric(7,2),department_id integer,address_id integer not null, version integer, constraint fk_department_id foreign key(department_id) references department(department_id), constraint fk_address_id foreign key(address_id) references address(address_id));
create table comp_key_department(department_id1 integer not null, department_id2 integer not null, department_no numeric(2) not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer, constraint pk_dept primary key(department_id1, department_id2));
create table comp_key_address(address_id1 integer not null, address_id2 integer not null, street varchar(20), version integer, constraint pk_add primary key(address_id1, address_id2));
create table comp_key_employee(employee_id1 integer not null, employee_id2 integer not null, employee_no numeric(4) not null ,employee_name varchar(20),manager_id1 integer,manager_id2 integer,hiredate date,salary numeric(7,2),department_id1 integer,department_id2 integer,address_id1 integer not null,address_id2 integer not null,version integer, constraint pk_emp primary key(employee_id1, employee_id2), constraint fk_dept_id foreign key(department_id1, department_id2) references comp_key_department(department_id1, department_id2), constraint fk_add_id foreign key(address_id1, address_id2) references comp_key_address(address_id1, address_id2));
alter table employee add constraint employee_uk1 unique (address_id);
alter table comp_key_employee add constraint comp_key_employee_uk1 unique (address_id1, address_id2);

comment on table address is '住所';
comment on column address.address_id is '識別子';
comment on column address.street is 'ストリート';
comment on column address.version is 'バージョン';

create table smallint_table (smallint_column smallint);
create table integer_table (integer_column integer);
create table bigint_table (bigint_column bigint);
create table decimal_table (decimal_column decimal);
create table real_table (real_column real);
create table double_table (double_column double);
create table char_table (char_column char);
create table char_bit_table (char_bit_column char for bit data);
create table varchar_table (varchar_column varchar(255));
create table varchar_bit_table (varchar_bit_column varchar(255) for bit data);
create table long_varchar_table (long_varchar_column long varchar);
create table long_varchar_bit_table (long_varchar_bit_column long varchar for bit data);
create table clob_table (clob_column clob);
create table blob_table (blob_column blob);
create table date_table (date_column date);
create table time_table (time_column time);
create table timestamp_table (timestamp_column timestamp);
create table テーブル (カラム varchar(255));

CREATE PROCEDURE PROC_DTO_PARAM(
  IN param1 INTEGER,
  INOUT param2 INTEGER,
  OUT param3 INTEGER)
LANGUAGE SQL
DYNAMIC RESULT SETS 0
BEGIN
  SET param2 = param2 + param1;
  SET param3 = param1;
END
@
