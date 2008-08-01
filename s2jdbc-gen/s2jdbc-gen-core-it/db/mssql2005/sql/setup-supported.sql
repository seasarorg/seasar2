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

CREATE FUNCTION [dbo].[FUNC_SIMPLETYPE_PARAM] (
    @param1 int)
RETURNS int
AS
BEGIN
    RETURN 20;
END
GO
