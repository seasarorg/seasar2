SELECT emp.*, dept.dname dname_0, dept.loc loc_0 FROM emp, dept
WHERE emp.deptno = dept.deptno ORDER BY emp.empno