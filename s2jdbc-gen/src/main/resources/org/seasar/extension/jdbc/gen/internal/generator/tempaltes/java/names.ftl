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
 * {@link ${shortEntityClassName}}のプロパティ名の集合です。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} {
<#list namesAttributeModelList as attr>

    /**
     * ${attr.name}のプロパティ名を返します。
     * 
     * @return ${attr.name}のプロパティ名
     */
    public static PropertyName<${attr.attributeClass.simpleName}> ${attr.name}() {
        return new PropertyName<${attr.attributeClass.simpleName}>("${attr.name}");
    }
</#list>
<#list namesAssociationModelList as asso>

    /**
     * ${asso.name}のプロパティ名を返します。
     * 
     * @return ${asso.name}のプロパティ名
     */
    public static ${asso.shortClassName} ${asso.name}() {
        return new ${asso.shortClassName}("${asso.name}");
    }
</#list>

    /**
     * @author S2JDBC-Gen
     */
    public static class ${shortInnerClassName} extends PropertyName<${shortEntityClassName}> {

        /**
         * インスタンスを構築します。
         */
        public ${shortInnerClassName}() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public ${shortInnerClassName}(final String name) {
            super(name);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param parent
         *            親
         * @param name
         *            名前
         */
        public ${shortInnerClassName}(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }
<#list namesAttributeModelList as attr>

        /**
         * ${attr.name}のプロパティ名を返します。
         *
         * @return ${attr.name}のプロパティ名
         */
        public PropertyName<${attr.attributeClass.simpleName}> ${attr.name}() {
            return new PropertyName<${attr.attributeClass.simpleName}>(this, "${attr.name}");
        }
</#list>
<#list namesAssociationModelList as asso>

        /**
         * ${asso.name}のプロパティ名を返します。
         * 
         * @return ${asso.name}のプロパティ名
         */
        public ${asso.shortClassName} ${asso.name}() {
            return new ${asso.shortClassName}(this, "${asso.name}");
        }
</#list>
    }
}