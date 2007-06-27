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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.S2MethodInvocation;
import org.seasar.framework.aop.impl.NestedMethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.container.impl.SimpleComponentDef;
import org.seasar.framework.util.StringUtil;

/**
 * {@link org.seasar.framework.container.ComponentDef コンポーネント定義}に
 * {@link org.seasar.framework.container.AspectDef アスペクト定義}を
 * 登録するコンポーネントカスタマイザです。
 * <p>
 * カスタマイザには、ポイントカットとインターセプタを設定します。 インターセプタはコンポーネント名で指定し、複数のインターセプタ名を設定することができます。
 * インターセプタ名が複数設定された場合は、設定された順にアスペクト定義をコンポーネント定義に登録します。
 * 最初に設定された名前を持つインターセプタが、後に設定された名前を持つインターセプタよりも先に呼び出されることになります。
 * </p>
 * <p>
 * コンポーネントに適用するインターセプタのインスタンス属性が<code>singleton</code>以外の場合は、
 * {@link #setUseLookupAdapter(boolean) useLookupAdapter}プロパティを<code>true</code>に設定します。
 * これにより、コンポーネントのメソッドが呼び出される度に、コンテナからインターセプタのインスタンスをルックアップするようになります。
 * </p>
 * 
 * @author higa
 */
public class AspectCustomizer extends AbstractCustomizer {

    /** <coce>interceptorName</code>プロパティのバインディング定義です。 */
    public static final String interceptorName_BINDING = "bindingType=may";

    /** <coce>pointcut</code>プロパティのバインディング定義です。 */
    public static final String pointcut_BINDING = "bindingType=may";

    /** <coce>useLookupAdapter</code>プロパティのバインディング定義です。 */
    public static final String useLookupAdapter_BINDING = "bindingType=may";

    private final List interceptorNames = new ArrayList();

    private String pointcut;

    private boolean useLookupAdapter;

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
     * インスタンス属性が<code>singleton</code>以外のインターセプタを適用する場合は<code>true</code>を、
     * そうでない場合は<code>false</code>を指定します。
     * 
     * @param useLookupAdapter
     *            インスタンス属性が<code>singleton</code>以外のインターセプタを適用する場合は<code>true</code>
     */
    public void setUseLookupAdapter(final boolean useLookupAdapter) {
        this.useLookupAdapter = useLookupAdapter;
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
        if (useLookupAdapter) {
            final MethodInterceptor adaptor = new LookupAdaptorInterceptor(
                    (String[]) interceptorNames
                            .toArray(new String[interceptorNames.size()]));
            final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                    new SimpleComponentDef(adaptor), createPointcut());
            componentDef.addAspectDef(aspectDef);
        } else {
            for (int i = 0; i < interceptorNames.size(); ++i) {
                final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                        (String) interceptorNames.get(i), createPointcut());
                componentDef.addAspectDef(aspectDef);
            }
        }
    }

    /**
     * ポイントカットを作成して返します。
     * <p>
     * <code>pointcut</code>プロパティが指定されている場合は、その文字列からポイントカットを作成します。
     * <code>targetInterface</code>プロパティが指定されている場合は、そのインターフェースからポイントカットを作成します。
     * それ以外の場合は<code>null</code>を返します。
     * </p>
     * 
     * @return ポイントカット
     */
    protected Pointcut createPointcut() {
        if (!StringUtil.isEmpty(pointcut)) {
            return AspectDefFactory.createPointcut(pointcut);
        }
        if (targetInterface != null) {
            return AspectDefFactory.createPointcut(targetInterface);
        }
        return null;
    }

    /**
     * インスタンス属性が<code>singleton</code>以外のインターセプタを呼び出すためのアダプタとなるインターセプタです。
     * 
     * @author koichik
     */
    public static class LookupAdaptorInterceptor extends AbstractInterceptor {

        private static final long serialVersionUID = 1L;

        /**
         * インターセプタ名の配列です。
         */
        protected String[] interceptorNames;

        /**
         * インスタンスを構築します。
         * 
         * @param interceptorNames
         *            インターセプタ名の配列
         */
        public LookupAdaptorInterceptor(final String[] interceptorNames) {
            this.interceptorNames = interceptorNames;
        }

        public Object invoke(final MethodInvocation invocation)
                throws Throwable {
            final S2Container container = getComponentDef(invocation)
                    .getContainer().getRoot();
            final MethodInterceptor[] interceptors = new MethodInterceptor[interceptorNames.length];
            for (int i = 0; i < interceptors.length; ++i) {
                interceptors[i] = (MethodInterceptor) container
                        .getComponent(interceptorNames[i]);
            }
            final MethodInvocation nestInvocation = new NestedMethodInvocation(
                    (S2MethodInvocation) invocation, interceptors);
            return nestInvocation.proceed();
        }

    }

}
