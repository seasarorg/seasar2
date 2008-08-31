package examples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

/**
 * Employeeエンティティクラスです。
 * 
 * @author S2JDBC-Gen
 */
@Entity
public class Employee {

    /** idプロパティ */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "integer")
    public Integer id;

    /** nameプロパティ */
    @Column(columnDefinition = "varchar(255)", nullable = false, unique = false)
    public String name;

    /** jobTypeプロパティ */
    @Column(columnDefinition = "integer", nullable = false, unique = false)
    public JobType jobType;

    /** salaryプロパティ */
    @Column(columnDefinition = "integer", nullable = true, unique = false)
    public Integer salary;

    /** departmentIdプロパティ */
    @Column(columnDefinition = "integer", nullable = true, unique = false)
    public Integer departmentId;

    /** addressIdプロパティ */
    @Column(columnDefinition = "integer", nullable = true, unique = true)
    public Integer addressId;

    /** versionプロパティ */
    @Version
    @Column(columnDefinition = "integer", nullable = false, unique = false)
    public Integer version;

    /** address関連プロパティ */
    @OneToOne
    public Address address;

    /** department関連プロパティ */
    @ManyToOne
    public Department department;
}