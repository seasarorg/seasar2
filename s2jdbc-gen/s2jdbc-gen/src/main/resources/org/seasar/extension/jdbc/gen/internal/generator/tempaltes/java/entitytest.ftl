<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
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
 * {@link ${shortEntityClassName}}のテストクラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
<#if useS2junit4>
@RunWith(Seasar2.class)
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} <#if !useS2junit4>extends S2TestCase </#if>{
<#if useS2junit4>

    private TestContext testContext;
</#if>

    private JdbcManager ${jdbcManagerName};

    /**
     * 事前処理をします。
     * 
     * @throws Exception
     */
<#if useS2junit4>
    public void before() throws Exception {
        testContext.setAutoIncluding(false);
        testContext.include("${configPath}");
    }
<#else>
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("${configPath}");
    }
</#if>

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
  <#if namesModel??>
    <#list namesModel.namesAssociationModelList as namesAssociationModel>

    /**
     * ${namesAssociationModel.name}との外部結合をテストします。
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_${namesAssociationModel.name}() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).leftOuterJoin(${namesAssociationModel.name}()).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
    }
    </#list>
  <#else>
    <#list associationNameList as associationName>

    /**
     * ${associationName}との外部結合をテストします。
     * 
     * @throws Exception
     */
    public void testLeftOuterJoin_${associationName}() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).leftOuterJoin("${associationName}").id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
    }
    </#list>
  </#if>
</#if>
}