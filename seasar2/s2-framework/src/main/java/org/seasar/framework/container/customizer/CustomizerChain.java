/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

/**
 * 複数のコンポーネントカスタマイザをチェーンとして連結するカスタマイザです。
 * 
 * @author higa
 */
public class CustomizerChain extends AbstractCustomizer {

    private List customizers = new ArrayList();

    /**
     * このカスタマイザチェーンに連結されているカスタマイザの数を返します。
     * 
     * @return 連結されているカスタマイザの数
     */
    public int getCustomizerSize() {
        return customizers.size();
    }

    /**
     * 指定された位置のコンポーネントカスタマイザを返します。
     * 
     * @param index
     *            取得するコンポーネントカスタマイザの位置
     * @return 指定された位置のコンポーネントカスタマイザ
     * @throws ArrayIndexOutOfBoundsException
     *             指定された位置が設定されているカスタマイザの範囲外の場合にスローされます
     */
    public ComponentCustomizer getCustomizer(int index) {
        return (ComponentCustomizer) customizers.get(index);
    }

    /**
     * チェーンの最後にコンポーネントカスタマイザを追加します。
     * 
     * @param customizer
     *            コンポーネントカスタマイザ
     */
    public void addCustomizer(ComponentCustomizer customizer) {
        customizers.add(customizer);
    }

    /**
     * チェーンの最後にアスペクトカスタマイザを追加します。
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     */
    public void addAspectCustomizer(final String interceptorName) {
        AspectCustomizer customizer = new AspectCustomizer();
        customizer.setInterceptorName(interceptorName);
        addCustomizer(customizer);
    }

    /**
     * チェーンの最後にアスペクトカスタマイザを追加します。
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     * @param pointcut
     *            ポイントカット
     */
    public void addAspectCustomizer(final String interceptorName,
            final String pointcut) {
        AspectCustomizer customizer = new AspectCustomizer();
        customizer.setInterceptorName(interceptorName);
        customizer.setPointcut(pointcut);
        addCustomizer(customizer);
    }

    /**
     * チェーンの最後にアスペクトカスタマイザを追加します。
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     * @param useLookupAdapter
     *            インスタンス属性が<code>singleton</code>以外のインターセプタを適用する場合は<code>true</code>
     */
    public void addAspectCustomizer(final String interceptorName,
            final boolean useLookupAdapter) {
        AspectCustomizer customizer = new AspectCustomizer();
        customizer.setInterceptorName(interceptorName);
        customizer.setUseLookupAdapter(useLookupAdapter);
        addCustomizer(customizer);
    }

    /**
     * チェーンの最後にアスペクトカスタマイザを追加します。
     * 
     * @param interceptorName
     *            インターセプタのコンポーネント名
     * @param pointcut
     *            ポイントカット
     * @param useLookupAdapter
     *            インスタンス属性が<code>singleton</code>以外のインターセプタを適用する場合は<code>true</code>
     */
    public void addAspectCustomizer(final String interceptorName,
            final String pointcut, final boolean useLookupAdapter) {
        AspectCustomizer customizer = new AspectCustomizer();
        customizer.setInterceptorName(interceptorName);
        customizer.setPointcut(pointcut);
        customizer.setUseLookupAdapter(useLookupAdapter);
        addCustomizer(customizer);
    }

    /**
     * カスタマイズ対象のコンポーネント定義をカスタマイズをします。
     * <p>
     * コンポーネント定義を、このカスタマイザチェーンに設定されているコンポーネントカスタマイザに、 設定されている順に適用します。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    public void doCustomize(ComponentDef componentDef) {
        for (int i = 0; i < getCustomizerSize(); ++i) {
            ComponentCustomizer customizer = getCustomizer(i);
            customizer.customize(componentDef);
        }
    }

}
