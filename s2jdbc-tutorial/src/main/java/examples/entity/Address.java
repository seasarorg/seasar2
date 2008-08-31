package examples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

/**
 * Addressエンティティクラスです。
 * 
 * @author S2JDBC-Gen
 */
@Entity
public class Address {

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

    /** employee関連プロパティ */
    @OneToOne(mappedBy = "address")
    public Employee employee;
}