package org.seasar.framework.container.cooldeploy;

import java.util.Arrays;

import org.seasar.framework.util.StringUtil;

public class DefaultConventionNaming implements ConventionNaming {

    public String defineName(String rootPackageName, String middlePackageName,
            String nameSuffix, String className) {

        String name = trimPackageName(className, rootPackageName);
        name = trimPackageName(name, middlePackageName);
        return composeName(nameSuffix, name);
    }

    public String defineName(String rootPackageName,
            String[] middlePackageNames, String nameSuffix, String className) {

        String name = trimPackageName(className, rootPackageName);
        name = trimMiddlePackageNames(name, middlePackageNames);
        return composeName(nameSuffix, name);
    }

    protected String trimPackageName(String name, String packageName) {
        String ret = trimPackageNameNoException(name, packageName);
        if (ret == null) {
            throw new IllegalArgumentException(name + ", " + packageName);
        }
        return ret;
    }

    protected String trimPackageNameNoException(String name, String packageName) {
        if (packageName == null) {
            return name;
        }
        int pos = name.indexOf(packageName);
        if (pos < 0) {
            return null;
        }
        return name.substring(pos + packageName.length() + 1);
    }

    protected String trimMiddlePackageNames(String name, String[] packageNames) {
        if (packageNames != null && packageNames.length > 0) {
            for (int i = 0; i < packageNames.length; ++i) {
                String s = trimPackageNameNoException(name, packageNames[i]);
                if (s != null) {
                    return s;
                }
            }
            throw new IllegalArgumentException(name + ", "
                    + Arrays.asList(packageNames));
        }
        return name;
    }

    protected String composeName(String nameSuffix, String name) {
        String[] names = StringUtil.split(name, ".");
        String lastName = names[names.length - 1];
        if (nameSuffix == null) {
            return StringUtil.decapitalize(lastName);
        }
        int pos = lastName.lastIndexOf(nameSuffix);
        if (pos < 0) {
            throw new IllegalArgumentException(nameSuffix + ", " + name);
        }
        boolean hasImplementatinPackage = lastName.length() > pos
                + nameSuffix.length();
        lastName = StringUtil.decapitalize(lastName.substring(0, pos))
                + nameSuffix;
        if (names.length == 3) {
            if (hasImplementatinPackage) {
                return names[0] + "_" + lastName;
            } else {
                throw new IllegalArgumentException(nameSuffix + ", " + name);
            }
        }
        if (names.length == 4) {
            if (hasImplementatinPackage) {
                return names[1] + "_" + lastName;
            } else {
                throw new IllegalArgumentException(nameSuffix + ", " + name);
            }
        }
        if (names.length == 2) {
            if (hasImplementatinPackage) {
                return lastName;
            } else {
                return names[0] + "_" + lastName;
            }
        }
        return lastName;
    }

}
