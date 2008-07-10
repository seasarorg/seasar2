package ${packageName};

<#list importPackageNameSet as importPackageName>
import ${importPackageName};
</#list>

public class ${shortClassName} extends S2TestCase {
<#list entityDesc.attributeDescList as attr>

    private JdbcManager jdbcManager;

    protected void setUp() throws Exception {
        super.setUp();
        include(${configPath});
    }

    public void testMapping() throws Exception {
        jdbcManager.from(${entityClassName}).id(<#list idValueList as value>value<#if value_has_next>, </if></#list>).execute();
    }
}