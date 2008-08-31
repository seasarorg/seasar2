package examples.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 * Departmentエンティティクラスです。
 * 
 * @author S2JDBC-Gen
 */
@Entity
public class Department {

    /** idプロパティ */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "integer")
    public Integer id;

    /** nameプロパティ */
    @Column(columnDefinition = "varchar(255)", nullable = false, unique = false)
    public String name;

    /** versionプロパティ */
    @Version
    @Column(columnDefinition = "integer", nullable = false, unique = false)
    public Integer version;

    /** employeeList関連プロパティ */
    @OneToMany(mappedBy = "department")
    public List<Employee> employeeList;
}