<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * 
 * @author S2JDBC-Gen
 */
public class ${shortClassName} extends S2TestCase {

    private JdbcManager ${jdbcManagerName};

    /**
     * 
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("${configPath}");
    }

    /**
     * 
     * @throws Exception
     */
    public void testFindById() throws Exception {
<#if idExpressionList?size == 0>
        ${jdbcManagerName}.from(${shortEntityClassName}.class).getResultList();
<#else>
        ${jdbcManagerName}.from(${shortEntityClassName}.class).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
</#if>
    }
}