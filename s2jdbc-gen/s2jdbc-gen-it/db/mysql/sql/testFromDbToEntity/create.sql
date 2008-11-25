create table department(department_id integer not null primary key, department_no integer not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer);
create table address(address_id integer not null primary key comment '識別子', street varchar(20) comment 'ストリート', version integer comment 'バージョン') comment = '住所';
create table employee(employee_id integer not null primary key, employee_no integer not null ,employee_name varchar(20),manager_id integer,hiredate date,salary numeric(7,2),department_id integer,address_id integer, version integer, constraint fk_department_id foreign key(department_id) references department(department_id),constraint fk_address_id foreign key(address_id) references address(address_id));
create table comp_key_department(department_id1 integer not null, department_id2 integer not null, department_no integer not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer, constraint pk_comp_key_department primary key(department_id1, department_id2));
create table comp_key_address(address_id1 integer not null, address_id2 integer not null, street varchar(20), version integer, constraint pk_comp_key_address primary key(address_id1, address_id2));
create table comp_key_employee(employee_id1 integer not null, employee_id2 integer not null, employee_no integer not null ,employee_name varchar(20),manager_id1 integer,manager_id2 integer,hiredate date,salary numeric(7,2),department_id1 integer,department_id2 integer,address_id1 integer,address_id2 integer,version integer, constraint pk_comp_key_employee primary key(employee_id1, employee_id2), constraint fk_comp_key_department_id foreign key(department_id1, department_id2) references comp_key_department(department_id1, department_id2), constraint fk_comp_key_address_id foreign key(address_id1, address_id2) references comp_key_address(address_id1, address_id2));
alter table employee add constraint employee_uk1 unique (address_id);
alter table comp_key_employee add constraint comp_key_employee_uk1 unique (address_id1, address_id2);

create table bit_table (bit_column bit);
create table tinyint_table (tinyint_column tinyint);
create table tinyint_unsigned_table (tinyint_unsigned_column tinyint unsigned);
create table bool_table (bool_column bool);
create table boolean_table (boolean_column boolean);
create table smallint_table (smallint_column smallint);
create table smallint_unsigned_table (smallint_unsigned_column smallint unsigned);
create table mediumint_table (mediumint_column mediumint);
create table mediumint_unsigned_table (mediumint_unsigned_column mediumint unsigned);
create table int_table (int_column int);
create table int_unsigned_table (int_unsigned_column int unsigned);
create table integer_table (integer_column integer);
create table integer_unsigned_table (integer_unsigned_column integer unsigned);
create table bigint_table (bigint_column bigint);
create table bigint_unsigned_table (bigint_unsigned_column bigint unsigned);
create table float_table (float_column float);
create table double_table (double_column double);
create table double_precision_table (double_precision_column double precision);
create table real_table (real_column real);
create table decimal_table (decimal_column decimal);
create table dec_table (dec_column dec);
create table numeric_table (numeric_column numeric);
create table datetime_table (datetime_column datetime);
create table date_table (date_column date);
create table timestamp_table (timestamp_column timestamp);
create table time_table (time_column time);
create table year_table (year_column year);
create table char_table (char_column char);
create table varchar_table (varchar_column varchar(255));
create table binary_table (binary_column binary);
create table varbinary_table (varbinary_column varbinary(255));
create table tinyblob_table (tinyblob_column tinyblob);
create table tinytext_table (tinytext_column tinytext);
create table blob_table (blob_column blob);
create table text_table (text_column text);
create table mediumblob_table (mediumblob_column mediumblob);
create table mediumtext_table (mediumtext_column mediumtext);
create table longblob_table (longblob_column longblob);
create table longtext_table (longtext_column longtext);
--create table テーブル (カラム varchar(255));

CREATE FUNCTION FUNC_SIMPLETYPE_PARAM(
  param1 INTEGER)
RETURNS INTEGER
BEGIN
  RETURN 20;
END
/