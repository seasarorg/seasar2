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

import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.factory.AspectDefFactory;

/**
 * {@link org.seasar.framework.container.ComponentDef コンポーネント定義}に
 * {@link org.seasar.framework.container.AspectDef アスペクト定義}を
 * 登録するコンポーネントカスタマイザです。
 * <p>
 * カスタマイザには、ポイントカットとインターセプタを設定します。 インターセプタはコンポーネント名で指定し、複数のインターセプタ名を設定することができます。
 * インターセプタ名が複数設定された場合は、設定された順にアスペクト定義をコンポーネント定義に登録します。
 * 最初に設定された名前を持つインターセプタが、後に設定された名前を持つインターセプタよりも先に呼び出されることになります。
 * </p>
 * 
 * @author higa
 */
public class AspectCustomizer extends AbstractCustomizer {

    private List interceptorNames = new ArrayList();

    private String pointcut;

    /**
     * コンポーネント定義に登録するインターセプタのコンポーネント名を設定します。
     * <p>
     * すでに設定されているインターセプタ名は破棄されます。
     * </p>
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     */
    public void setInterceptorName(final String interceptorName) {
        interceptorNames.clear();
        interceptorNames.add(interceptorName);
    }

    /**
     * コンポーネント定義に登録するインターセプタのコンポーネント名を追加します。
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     */
    public void addInterceptorName(final String interceptorName) {
        interceptorNames.add(interceptorName);
    }

    /**
     * コンポーネント定義に登録するアスペクト定義のポイントカットを設定します。
     * 
     * @param pointcut
     *            ポイントカット
     */
    public void setPointcut(final String pointcut) {
        this.pointcut = pointcut;
    }

    /**
     * カスタマイズ対象のコンポーネント定義をカスタマイズをします。
     * <p>
     * 設定されたインターセプタ名を持つアスペクト定義をコンポーネント定義に登録します。
     * インターセプタ名が複数設定された場合は、設定された順にアスペクト定義をコンポーネント定義に登録します。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void doCustomize(final ComponentDef componentDef) {
        for (int i = 0; i < interceptorNames.size(); ++i) {
            AspectDef aspectDef = AspectDefFactory.createAspectDef(
                    (String) interceptorNames.get(i), pointcut);
            componentDef.addAspectDef(aspectDef);
        }
    }
}