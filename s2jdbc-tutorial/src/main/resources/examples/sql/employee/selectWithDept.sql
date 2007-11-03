select e.*, d.name as department_name
from employee e left outer join department d on e.department_id = d.id
where
e.salary >= /*salaryMin*/1000
and e.salary <= /*salaryMax*/2000
order by e.salary