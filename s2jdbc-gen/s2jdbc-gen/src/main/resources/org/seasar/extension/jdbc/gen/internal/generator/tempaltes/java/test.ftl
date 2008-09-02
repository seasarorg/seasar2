<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * {@link ${shortEntityClassName}}のテストクラスです。
 * 
 * @author S2JDBC-Gen
 */
public class ${shortClassName} extends S2TestCase {

    private JdbcManager ${jdbcManagerName};

    /**
     * 事前処理をします。
     * 
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("${configPath}");
    }

<#if idExpressionList?size == 0>
    /**
     * 全件取得をテストします。
     * 
     * @throws Exception
     */
    public void testFindAll() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).getResultList();
    }
<#else>
    /**
     * 識別子による取得をテストします。
     * 
     * @throws Exception
     */
    public void testFindById() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
    }
</#if>
}