/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.customizer;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.autoregister.ClassPattern;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link org.seasar.framework.container.ComponentDef コンポーネント定義}をカスタマイズする
 * コンポーネントカスタマイザの抽象クラスです。
 * <p>
 * カスタマイズ対象となるコンポーネントおよびカスタマイズ非対象のコンポーネントを
 * {@link org.seasar.framework.container.autoregister.ClassPattern クラスパターン}で
 * 指定することができます。
 * <ul>
 * <li>カスタマイズ対象・非対称のクラスパターン共に指定されなかった場合は、全てのコンポーネントを カスタマイズ対象とします。</li>
 * <li>カスタマイズ対象のクラスパターンのみ指定された場合は、カスタマイズ対象クラスパターンに
 * マッチしたコンポーネントのみがカスタマイズの対象となります。</li>
 * <li>カスタマイズ非対象のクラスパターンのみ指定された場合は、カスタマイズ非対象クラスパターンに
 * マッチしなかったコンポーネントのみがカスタマイズの対象となります。</li>
 * <li>カスタマイズ対象と非対象のクラスパターンが共に指定された場合は、カスタマイズ対象クラスパターンに
 * マッチしてかつ、カスタマイズ非対象のクラスパターンにマッチしなかったコンポーネントのみがカスタマイズの対象となります。</li>
 * </ul>
 * </p>
 * <p>
 * {@link #customize(ComponentDef)}メソッドの引数で渡されたコンポーネントがカスタマイズ対象の場合は、 抽象メソッド{@link #doCustomize(ComponentDef)}メソッドを呼び出します。
 * サブクラスは{@link #doCustomize(ComponentDef)}メソッドを実装してコンポーネント定義をカスタマイズしてください。
 * </p>
 * 
 * @author koichik
 */
public abstract class AbstractCustomizer implements ComponentCustomizer {

    /** カスタマイズ対象のクラスパターン */
    protected final List classPatterns = new ArrayList();

    /** カスタマイズ非対象のクラスパターン */
    protected final List ignoreClassPatterns = new ArrayList();

    /**
     * カスタマイズ対象のクラスパターンを追加します。
     * 
     * @param packageName
     *            カスタマイズ対象のパッケージ名
     * @param shortClassNames
     *            カスタマイズ対象のクラス名
     */
    public void addClassPattern(final String packageName,
            final String shortClassNames) {

        addClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    /**
     * カスタマイズ対象のクラスパターンを追加します。
     * 
     * @param classPattern
     *            カスタマイズ対象のクラスパターン
     */
    public void addClassPattern(final ClassPattern classPattern) {
        classPatterns.add(classPattern);
    }

    /**
     * カスタマイズ非対象のクラスパターンを追加します。
     * 
     * @param packageName
     *            カスタマイズ非対象のパッケージ名
     * @param shortClassNames
     *            カスタマイズ非対象のクラス名
     */
    public void addIgnoreClassPattern(final String packageName,
            final String shortClassNames) {

        addIgnoreClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    /**
     * カスタマイズ非対象のクラスパターンを追加します。
     * 
     * @param classPattern
     *            カスタマイズ非対象のクラスパターン
     */
    public void addIgnoreClassPattern(final ClassPattern classPattern) {
        ignoreClassPatterns.add(classPattern);
    }

    /**
     * コンポーネント定義をカスタマイズをします。
     * <p>
     * componentDefがカスタマイズ対象の場合は、{@link #doCustomize()}メソッドを呼び出します。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    public void customize(final ComponentDef componentDef) {
        if (isMatch(componentDef)) {
            doCustomize(componentDef);
        }
    }

    /**
     * コンポーネント定義がカスタマイズ対象かどうかを判定します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @return コンポーネント定義がカスタマイズ対象なら<code>true</code>、そうでない場合は<code>false</code>
     */
    protected boolean isMatch(final ComponentDef componentDef) {
        if (classPatterns.isEmpty() && ignoreClassPatterns.isEmpty()) {
            return true;
        }
        final Class clazz = componentDef.getComponentClass();
        final String packageName = ClassUtil.getPackageName(clazz);
        final String shortClassName = ClassUtil.getShortClassName(clazz);
        for (int i = 0; i < ignoreClassPatterns.size(); ++i) {
            final ClassPattern cp = (ClassPattern) ignoreClassPatterns.get(i);
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                return false;
            }
        }
        if (classPatterns.isEmpty()) {
            return true;
        }
        for (int i = 0; i < classPatterns.size(); ++i) {
            final ClassPattern cp = (ClassPattern) classPatterns.get(i);
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * カスタマイズ対象のコンポーネント定義をカスタマイズをします。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected abstract void doCustomize(final ComponentDef componentDef);

}
