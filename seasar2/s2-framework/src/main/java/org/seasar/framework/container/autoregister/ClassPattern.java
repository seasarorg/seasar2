/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * 自動登録の対象、非対象となるクラス名のパターンを保持します。
 * 
 * @author higa
 * 
 */
public class ClassPattern {

    private String packageName;

    private Pattern[] shortClassNamePatterns;

    /**
     * デフォルトのコンストラクタです。
     */
    public ClassPattern() {
    }

    /**
     * パッケージ名とクラス名のパターンを受け取るコンストラクタです。
     * 
     * @param packageName
     * @param shortClassNames
     */
    public ClassPattern(String packageName, String shortClassNames) {
        setPackageName(packageName);
        setShortClassNames(shortClassNames);
    }

    /**
     * パッケージ名を返します。
     * 
     * @return
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * パッケージ名を設定します。
     * 
     * @param packageName
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * (パッケージを含まない)クラス名のパターンを設定します。複数のパターンを設定する場合、','で区切ります。
     * 
     * @param shortClassNames
     */
    public void setShortClassNames(String shortClassNames) {
        String[] classNames = StringUtil.split(shortClassNames, ",");
        shortClassNamePatterns = new Pattern[classNames.length];
        for (int i = 0; i < classNames.length; ++i) {
            String s = classNames[i].trim();
            shortClassNamePatterns[i] = Pattern.compile(s);
        }
    }

    /**
     * (パッケージを含まない)クラス名がパターンに一致しているかどうかを返します。
     * 
     * @param shortClassName
     * @return
     */
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

    /**
     * パッケージ名がパターンに一致しているかどうかを返します。
     * 
     * @param pName
     * @return
     */
    public boolean isAppliedPackageName(String pName) {
        if (!StringUtil.isEmpty(pName) && !StringUtil.isEmpty(packageName)) {
            return appendDelimiter(pName).startsWith(
                    appendDelimiter(packageName));
        }
        if (StringUtil.isEmpty(pName) && StringUtil.isEmpty(packageName)) {
            return true;
        }
        return false;
    }

    /**
     * デリミタを追加します。
     * 
     * @param name
     * @return デリミタが追加された名前
     */
    protected static String appendDelimiter(final String name) {
        return name.endsWith(".") ? name : name + ".";
    }
}
