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
package org.seasar.framework.container.factory;

import java.lang.reflect.Field;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.PropertyDef;

/**
 * Tigerアノテーションを読み取り{@link PropertyDef}を作成するインターフェースです。
 * 
 * @author koichik
 */
public interface PropertyDefBuilder {

    /**
     * コンポーネントからTigerアノテーションを読み取り{@link PropertyDef}を作成します。
     * 
     * @param annotationHandler
     *            このメソッドを呼び出しているアノテーションハンドラ
     * @param beanDesc
     *            コンポーネントの{@link BeanDesc ビーン定義}
     * @param propertyDesc
     *            対象の{@link PropertyDesc プロパティ定義}
     * @return {@link PropertyDef}
     */
    PropertyDef createPropertyDef(AnnotationHandler annotationHandler,
            BeanDesc beanDesc, PropertyDesc propertyDesc);

    /**
     * コンポーネントからTigerアノテーションを読み取り{@link PropertyDef}を作成します。
     * 
     * @param annotationHandler
     *            このメソッドを呼び出しているアノテーションハンドラ
     * @param beanDesc
     *            コンポーネントの{@link BeanDesc ビーン定義}
     * @param field
     *            対象の{@link Field フィールド}
     * @return {@link PropertyDef}
     */
    PropertyDef createPropertyDef(AnnotationHandler annotationHandler,
            BeanDesc beanDesc, Field field);

}
