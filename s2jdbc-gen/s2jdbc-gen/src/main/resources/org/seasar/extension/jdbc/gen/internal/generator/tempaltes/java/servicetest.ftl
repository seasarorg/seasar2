<#include "/copyright.ftl">
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import static ${importName};
  </#list>
</#if>

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
     * {@link #${shortServiceClassName?uncap_first}}が利用可能であることをテストします。
     * <p>
     * このテストが成功すれば、{@link #${shortServiceClassName?uncap_first}}に対するDIが正常に行われているとみなせます。
     * </p>
     *
     * @throws Exception
     */
    public void testAvailable() throws Exception {
    }
}