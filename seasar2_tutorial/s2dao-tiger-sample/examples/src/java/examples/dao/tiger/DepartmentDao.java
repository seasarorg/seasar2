package examples.dao.tiger;

import org.seasar.dao.annotation.tiger.S2Dao;

import examples.dao.tiger.Department;

@S2Dao(bean = Department.class)
public interface DepartmentDao {
	
    public void insert(Department department);

    public void update(Department department);

    public void delete(Department department);
}
