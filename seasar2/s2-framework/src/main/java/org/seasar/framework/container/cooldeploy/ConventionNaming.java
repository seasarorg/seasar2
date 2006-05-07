package org.seasar.framework.container.cooldeploy;

public interface ConventionNaming {

    String defineName(String rootPackageName, String middlePackageName,
            String nameSuffix, String className);
    
    String defineName(String rootPackageName, String[] middlePackageNames,
            String nameSuffix, String className);
}
