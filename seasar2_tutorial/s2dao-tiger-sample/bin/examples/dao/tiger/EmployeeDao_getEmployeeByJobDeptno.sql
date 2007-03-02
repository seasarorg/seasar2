SELECT * FROM emp
/*BEGIN*/WHERE
  /*IF job != null*/job = /*job*/'CLERK'/*END*/
  /*IF deptno != null*/AND deptno = /*deptno*/20/*END*/
/*END*/