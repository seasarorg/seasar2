package ${packageName};

<#list importPackageNameSet as name>
import ${name};
</#list>

public class ${shortClassName} extends ${shortBaseClassName} {
}