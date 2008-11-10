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
<#if useS2junit4>
@RunWith(Seasar2.class)
</#if>
public class ${shortClassName} <#if !useS2junit4>extends S2TestCase </#if>{

    private ${shortServiceClassName} ${shortServiceClassName?uncap_first};
<#if !useS2junit4>

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
</#if>

    /**
     * {@link ${shortServiceClassName}#getCount()}が動作することをテストします。
     * 
     * @throws Exception
     */
    public void testGetCountTx() throws Exception {
        ${shortServiceClassName?uncap_first}.getCount();
    }
}