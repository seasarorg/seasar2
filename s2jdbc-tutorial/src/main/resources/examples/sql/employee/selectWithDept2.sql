select e.*, d.name as department_name
from employee e left outer join department d on e.department_id = d.id
/*BEGIN*/
where
  /*IF salaryMin != null*/
    e.salary >= /*salaryMin*/1000
  /*END*/
  /*IF salaryMax != null*/
    and e.salary <= /*salaryMax*/2000
  /*END*/
/*END*/
order by e.salary