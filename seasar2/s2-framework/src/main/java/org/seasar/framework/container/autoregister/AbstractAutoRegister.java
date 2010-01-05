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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * 自動登録用の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractAutoRegister {

    /**
     * initメソッドアノテーションの定義です。
     */
    public static final String INIT_METHOD = "registerAll";

    private S2Container container;

    private List classPatterns = new ArrayList();

    private List ignoreClassPatterns = new ArrayList();

    /**
     * コンテナを返します。
     * 
     * @return
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * コンテナを設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * 追加されているClassPatternの数を返します。
     * 
     * @return
     */
    public int getClassPatternSize() {
        return classPatterns.size();
    }

    /**
     * ClassPatternを返します。
     * 
     * @param index
     * @return
     */
    public ClassPattern getClassPattern(int index) {
        return (ClassPattern) classPatterns.get(index);
    }

    /**
     * 自動登録で適用されるClassPatternを追加します。
     * 
     * @param packageName
     * @param shortClassNames
     */
    public void addClassPattern(String packageName, String shortClassNames) {

        addClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    /**
     * 自動登録で適用されるClassPatternを追加します。
     * 
     * @param classPattern
     */
    public void addClassPattern(ClassPattern classPattern) {
        classPatterns.add(classPattern);
    }

    /**
     * 自動登録されないClassPatternを追加します。
     * 
     * @param packageName
     * @param shortClassNames
     */
    public void addIgnoreClassPattern(String packageName, String shortClassNames) {

        addIgnoreClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    /**
     * 自動登録されないClassPatternを追加します。
     * 
     * @param classPattern
     */
    public void addIgnoreClassPattern(ClassPattern classPattern) {
        ignoreClassPatterns.add(classPattern);
    }

    /**
     * 自動登録を行います。
     */
    public abstract void registerAll();

    /**
     * {@link ComponentDef}があるかどうかを返します。
     * 
     * @param name
     * @return {@link ComponentDef}があるかどうか
     */
    protected boolean hasComponentDef(String name) {
        return findComponentDef(name) != null;
    }

    /**
     * {@link ComponentDef}を検索します。
     * 
     * @param name
     * @return {@link ComponentDef}
     */
    protected ComponentDef findComponentDef(String name) {
        if (name == null) {
            return null;
        }
        S2Container container = getContainer();
        for (int i = 0; i < container.getComponentDefSize(); ++i) {
            ComponentDef cd = container.getComponentDef(i);
            if (name.equals(cd.getComponentName())) {
                return cd;
            }
        }
        return null;
    }

    /**
     * 無視するかどうかを返します。
     * 
     * @param packageName
     * @param shortClassName
     * @return 無視するかどうか
     */
    protected boolean isIgnore(String packageName, String shortClassName) {
        if (ignoreClassPatterns.isEmpty()) {
            return false;
        }
        for (int i = 0; i < ignoreClassPatterns.size(); ++i) {
            ClassPattern cp = (ClassPattern) ignoreClassPatterns.get(i);
            if (!cp.isAppliedPackageName(packageName)) {
                continue;
            }
            if (cp.isAppliedShortClassName(shortClassName)) {
                return true;
            }
        }
        return false;
    }
}