DROP FUNCTION no_param();
DROP FUNCTION simpletype_param(param1 integer);
DROP FUNCTION dto_param(IN param1 integer, INOUT param2 integer, OUT param3 integer);
DROP FUNCTION one_result(OUT cur refcursor, IN employeeid integer);
DROP FUNCTION two_results(OUT empcur refcursor, OUT deptcur refcursor, IN employeeid integer, IN departmentid integer);

DROP TABLE EMPLOYEE;
DROP TABLE ADDRESS;
DROP TABLE DEPARTMENT;

DROP TABLE COMP_KEY_EMPLOYEE;
DROP TABLE COMP_KEY_ADDRESS;
DROP TABLE COMP_KEY_DEPARTMENT;
