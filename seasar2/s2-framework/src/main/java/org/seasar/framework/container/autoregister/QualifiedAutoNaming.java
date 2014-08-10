/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * 拡張されたAutoNamingです。
 * 
 * @author koichik
 */
public class QualifiedAutoNaming extends AbstractAutoNaming {

    /**
     * 無視するパッケージプレフィックスを追加します。
     * 
     * @param packagePrefix
     */
    public void addIgnorePackagePrefix(final String packagePrefix) {
        String regex = "^" + StringUtil.replace(packagePrefix, ".", "\\.");
        if (!regex.endsWith(".")) {
            regex += "\\.";
        }
        addReplaceRule(regex, "");
    }

    protected String makeDefineName(final String packageName,
            final String shortClassName) {
        return applyRule(ClassUtil.concatName(packageName, shortClassName));
    }
}
