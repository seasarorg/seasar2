package ${packageName};

<#list importPackageNames as name>
import ${name};
</#list>

@Entity
public class ${shortClassName} extends ${shortBaseClassName} {
}