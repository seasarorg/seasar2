create table binary_double_type (binary_double_col binary_double);
create table binary_float_type (binary_float_col binary_float);
create table blob_type (blob_col blob);
create table char_type (char_col char(1));
create table clob_type (clob_col clob);
create table date_type (date_col date);
create table float_type (float_col float);
create table long_type (long_col long);
create table long_raw_type (long_raw_col long raw);
create table nchar_type (nchar_col nchar(1));
create table number_type (number_col number(10,5));
create table nvarchar2_type (nvarchar2_col nvarchar2(2000));
create table raw_type (raw_col raw(2000));
create table timestamp_type (timestamp_col timestamp);
create table urowid_type (urowid_col urowid (4000));
create table varchar2_type (varchar2_col varchar2(4000));

create table schema_info (version number(8));
insert into schema_info values(0);