create table department(department_id integer not null primary key, department_no integer not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer);
create table address(address_id integer not null primary key, street varchar(20), version integer);
create table employee(employee_id integer not null primary key, employee_no integer not null ,employee_name varchar(20),manager_id integer,hiredate date,salary numeric(7,2),department_id integer,address_id integer, version integer, constraint fk_department_id foreign key(department_id) references department(department_id), constraint fk_address_id foreign key(address_id) references address(address_id));
create table comp_key_department(department_id1 integer not null, department_id2 integer not null, department_no integer not null unique,department_name varchar(20),location varchar(20) default 'tokyo', version integer, constraint pk_comp_key_department primary key(department_id1, department_id2));
create table comp_key_address(address_id1 integer not null, address_id2 integer not null, street varchar(20), version integer, constraint pk_comp_key_address primary key(address_id1, address_id2));
create table comp_key_employee(employee_id1 integer not null, employee_id2 integer not null, employee_no integer not null ,employee_name varchar(20),manager_id1 integer,manager_id2 integer,hiredate date,salary numeric(7,2),department_id1 integer,department_id2 integer,address_id1 integer,address_id2 integer,version integer, constraint pk_comp_key_employee primary key(employee_id1, employee_id2), constraint fk_comp_key_department_id foreign key(department_id1, department_id2) references comp_key_department(department_id1, department_id2), constraint fk_comp_key_address_id foreign key(address_id1, address_id2) references comp_key_address(address_id1, address_id2));
alter table employee add constraint employee_uk1 unique (address_id);
alter table comp_key_employee add constraint comp_key_employee_uk1 unique (address_id1, address_id2);

comment on table address is '住所';
comment on column address.address_id is '識別子';
comment on column address.street is 'ストリート';
comment on column address.version is 'バージョン';

create table bigint_table (bigint_column bigint);
create table bigserial_table (bigserial_column bigserial);
create table bit_table (bit_column bit);
create table bit_varying_table (bit_varying_column bit varying);
create table boolean_table (boolean_column boolean);
create table character_varying_table (character_varying_column character varying);
create table character_table (character_column character);
create table date_table (date_column date);
create table double_precision_table (double_precision_column double precision);
create table integer_table (integer_column integer);
create table money_table (money_column money);
create table real_table (real_column real);
create table smallint_table (smallint_column smallint);
create table serial_table (serial_column serial);
create table text_table (text_column text);
create table time_table (time_column time);
create table timestamp_table (timestamp_column timestamp);
create table int8_table (int8_column int8);
create table serial8_table (serial8_column serial8);
create table varbit_table (varbit_column varbit);
create table bool_table (bool_column bool);
create table varchar_table (varchar_column varchar(100));
create table char_table (char_column char);
create table float8_table (float8_column float8);
create table int_table (int_column int);
create table int4_table (int4_column int4);
create table numeric_table (numeric_column numeric);
create table decimal_table (decimal_column decimal);
create table float4_table (float4_column float4);
create table int2_table (int2_column int2);
create table serial4_table (serial4_column serial4);
create table timetz_table (timetz_column timetz);
create table timestamptz_table (timestamptz_column timestamptz);
create table テーブル (カラム varchar(255));

CREATE OR REPLACE FUNCTION FUNC_SIMPLETYPE_PARAM(
  param1 IN INTEGER) RETURNS INTEGER 
AS $$
BEGIN
  RETURN 20;
END;
$$ language plpgsql;
