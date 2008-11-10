<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>

/**
 * {@link ${shortServiceClassName}}のテストクラスです。
 * 
 * @author S2JDBC-Gen
 */
public class ${shortClassName} extends S2TestCase {

    private ${shortServiceClassName} ${shortServiceClassName?uncap_first};

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

    /**
     * {@link ${shortServiceClassName}#getCount()}が動作することをテストします。
     * 
     * @throws Exception
     */
    public void testGetCountTx() throws Exception {
        ${shortServiceClassName?uncap_first}.getCount();
    }
}