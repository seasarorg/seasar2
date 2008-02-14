package ${packageName};

<#list imports as import>
import ${import};
</#list>

@Entity
public class ${shortClassName} extends ${gapShortClassName} {
}