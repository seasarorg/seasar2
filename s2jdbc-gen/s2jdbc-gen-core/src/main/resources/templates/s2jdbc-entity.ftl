package ${packageName};

<#list importPackageNameSet as name>
import ${name};
</#list>

@Entity
public class ${shortClassName} extends ${shortBaseClassName} {
}