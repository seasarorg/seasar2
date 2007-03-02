select empno,ename,job,mgr,hiredate,
sal,comm,e.deptno,e.versionno,d.dname
from emp e left outer join dept d on e.deptno = d.deptno
where
empno = /*empno*/7788