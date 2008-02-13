package ${package};

<#list imports as import>
import ${import};
</#list>

@Entity
public class ${entityModel.name} extends ${gapClassName} {
}