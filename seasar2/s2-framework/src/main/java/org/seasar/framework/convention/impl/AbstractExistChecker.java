/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.convention.impl;

import org.seasar.framework.convention.ExistChecker;

/**
 * 存在チェッカのための基底クラスです。
 * 
 * @author y-komori
 */
public abstract class AbstractExistChecker implements ExistChecker {
    /**
     * パス名を返します。
     * 
     * @param lastClassName
     *            クラス名の最後
     * @return パス名
     */
    protected static String getPathName(final String lastClassName) {
        return lastClassName.replace('.', '/') + ".class";
    }
}
