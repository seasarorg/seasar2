/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.autoregister;

import java.util.regex.Pattern;

import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 *
 */
public class ClassPattern {

    private String packageName;
    
    private Pattern[] shortClassNamePatterns;
    
    public ClassPattern() {
    }
    
    public ClassPattern(String packageName, String shortClassNames) {
        setPackageName(packageName);
        setShortClassNames(shortClassNames);
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public void setShortClassNames(String shortClassNames) {
        String[] classNames = StringUtil.split(shortClassNames, ",");
        shortClassNamePatterns = new Pattern[classNames.length];
        for (int i = 0; i < classNames.length; ++i) {
            String s = classNames[i].trim();
            shortClassNamePatterns[i] = Pattern.compile(s);
        }
    }
    
    public boolean isAppliedShortClassName(String shortClassName) {
        if (shortClassNamePatterns == null) {
            return true;
        }
        for (int i = 0; i < shortClassNamePatterns.length; ++i) {
            if (shortClassNamePatterns[i].matcher(shortClassName).matches()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAppliedPackageName(String pName) {
        return pName == null ? packageName == null :
            pName.startsWith(packageName);
    }
}
