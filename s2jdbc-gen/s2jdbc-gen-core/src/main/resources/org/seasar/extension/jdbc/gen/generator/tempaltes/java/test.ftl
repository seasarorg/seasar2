package ${packageName};

<#list importPackageNameSet as importPackageName>
import ${importPackageName};
</#list>

public class ${shortClassName} extends S2TestCase {

    private JdbcManager ${jdbcManagerName};

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("${configPath}");
    }

    public void testFindById() throws Exception {
<#if idValueList?size == 0>
        ${jdbcManagerName}.from(${shortEntityClassName}.class).getResultList();
<#else>
        ${jdbcManagerName}.from(${shortEntityClassName}.class).id(<#list idValueList as idValue>${idValue}<#if idValue_has_next>, </#if></#list>).getSingleResult();
</#if>
    }
}