package examples.dao.tiger;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class DepartmentDaoClient {

    private static final String PATH = "examples/dao/tiger/DepartmentDao.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        try {
            DepartmentDao dao = (DepartmentDao) container
                    .getComponent(DepartmentDao.class);
            Department dept = new Department();
            dept.setDeptno(99);
            dept.setDname("foo");
            dao.insert(dept);
            dept.setDname("bar");
            System.out
                    .println("before update versionNo:" + dept.getVersionNo());
            dao.update(dept);
            System.out.println("after update versionNo:" + dept.getVersionNo());
            dao.delete(dept);
        } finally {
            container.destroy();
        }
    }
}