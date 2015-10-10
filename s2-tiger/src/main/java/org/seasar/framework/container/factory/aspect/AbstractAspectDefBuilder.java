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
package org.seasar.framework.container.factory.aspect;

import java.lang.reflect.Method;

import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.factory.AspectDefBuilder;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * Tigerアノテーションを読み取り{@link AspectDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractAspectDefBuilder implements AspectDefBuilder {

    /**
     * 指定のインターセプタとポイントカットを持つ{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param interceptor
     *            インターセプタを示すOGNL式
     * @param pointcut
     *            ポイントカットを示す文字列 (カンマ区切りの正規表現)
     */
    protected void appendAspect(final ComponentDef componentDef,
            final String interceptor, final String pointcut) {
        if (interceptor == null) {
            throw new EmptyRuntimeException("interceptor");
        }
        final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                interceptor, pointcut);
        componentDef.addAspectDef(aspectDef);
    }

    /**
     * 指定のインターセプタを指定のメソッドに適用する{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param interceptor
     *            インターセプタを示すOGNL式
     * @param pointcut
     *            インターセプタを適用するメソッド
     */
    protected void appendAspect(final ComponentDef componentDef,
            final String interceptor, final Method pointcut) {
        if (interceptor == null) {
            throw new EmptyRuntimeException("interceptor");
        }
        final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                interceptor, pointcut);
        componentDef.addAspectDef(aspectDef);
    }

}
