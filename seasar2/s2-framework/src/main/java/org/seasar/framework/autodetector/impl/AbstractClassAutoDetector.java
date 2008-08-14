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
package org.seasar.framework.autodetector.impl;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.autodetector.ClassAutoDetector;

/**
 * {@link ClassAutoDetector}の抽象クラスです。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractClassAutoDetector implements ClassAutoDetector {

    private List targetPackageNames = new ArrayList();;

    /**
     * {@link AbstractClassAutoDetector}のデフォルトコンストラクタです。
     */
    public AbstractClassAutoDetector() {
    }

    /**
     * ターゲットのパッケージ名を追加します。
     * 
     * @param targetPackageName
     */
    public void addTargetPackageName(final String targetPackageName) {
        targetPackageNames.add(targetPackageName);
    }

    /**
     * ターゲットのパッケージ名の数を返します。
     * 
     * @return
     */
    public int getTargetPackageNameSize() {
        return targetPackageNames.size();
    }

    /**
     * ターゲットのパッケージ名を返します。
     * 
     * @param index
     * @return
     */
    public String getTargetPackageName(final int index) {
        return (String) targetPackageNames.get(index);
    }

}
