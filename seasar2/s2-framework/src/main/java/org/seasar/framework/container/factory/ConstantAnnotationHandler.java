/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.IllegalDestroyMethodAnnotationRuntimeException;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.InterTypeDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.StringUtil;

/**
 * クラスに指定された定数アノテーションからコンポーネント定義を作成する実装クラスです。
 * <p>
 * 定数アノテーションとは<code>public</code>,<code>static</code>,<code>final</code>を用いて記述されたフィールドの事です。
 * フィールドのキーと値は次の形式で指定します。
 * 
 * <pre>
 * <var>key1</var>=<var>value1</var>,<var>key2</var>=<var>value2</var>,<var>key3</var>=<var>value3</var>....
 * </pre>
 * 
 * 指定出来るキーとその値はそれぞれの定数アノテーションで説明します。
 * 一部の定数アノテーションのフィールドにはキーとその値を指定することが出来ないものもあります。
 * 
 * フィールドの値が不正な場合は{@link IllegalArgumentException}をスローします。
 * </p>
 * 
 * <h4><code>Component</code>アノテーション</h4>
 * <p>
 * <code>Component</code>アノテーションは、フィールド名が<code>COMPONENT</code>で<code>String</code>型のフィールドです。
 * <code>Component</code>アノテーションであるフィールドに指定出来るキーは以下になります。
 * </p>
 * <dl>
 * <dt><code>name</code></dt>
 * <dd>コンポーネント名</dd>
 * <dt><code>instance</code></dt>
 * <dd>インスタンス定義</dd>
 * <dt><code>autoBinding</code></dt>
 * <dd>自動バインディング定義</dd>
 * <dt><code>externalBinding</code></dt>
 * <dd>外部バインディング定義</dd>
 * </dl>
 * <p>
 * 指定されたキーとその値に従ってコンポーネント定義を作成します。
 * </p>
 * 
 * <h4><code>Binding</code>アノテーション</h4>
 * <p>
 * <code>Binding</code>アノテーションは、フィールド名が<code>プロパティ名_BINDING</code>で<code>String</code>型のフィールドです。
 * <code>Binding</code>アノテーションであるフィールドに指定出来るキーは以下になります。
 * </p>
 * <dl>
 * <dt><code>bindingType</code></dt>
 * <dd>バインディングタイプ</dd>
 * <dt><code>value</code></dt>
 * <dd>ONGL式</dd>
 * </dl>
 * また、<code>Binding</code>アノテーションのフィールドには<code>null</code>や<code>ONGL</code>式を直接指定することも出来ます。
 * <p>
 * 指定されたキーとその値に従ってプロパティ定義を作成します。
 * </p>
 * 
 * <h4><code>Aspect</code>アノテーション</h4>
 * <code>Aspect</code>アノテーションは、フィールド名が<code>ASPECT</code>で<code>String</code>型のフィールドです。
 * <code>Aspect</code>アノテーションであるフィールドに指定出来るキーは以下になります。
 * 
 * <dl>
 * <dt><code>value</code></dt>
 * <dd>インターセプタ名</dd>
 * <dt><code>pointcut</code></dt>
 * <dd>ポイントカット</dd>
 * </dl>
 * <p>
 * 指定されたキーとその値に従ってアスペクト定義を作成します。
 * </p>
 * 
 * 
 * <h4><code>InterType</code>アノテーション</h4>
 * <p>
 * <code>InterType</code>アノテーションは、フィールド名が<code>INTER_TYPE</code>で<code>String</code>型のフィールドです。
 * <code>InterType</code>アノテーションであるフィールドには、ONGL式を指定します。
 * </p>
 * <p>
 * 指定されたONGL式を元にインタータイプ定義を作成し、コンポーネント定義に追加します。
 * </p>
 * 
 * 
 * <h4><code>InitMethod</code>アノテーション</h4>
 * <p>
 * <code>InitMethod</code>アノテーションは、フィールド名が<code>INIT_METHOD</code>で<code>String</code>型のフィールドです。
 * <code>InitMethod</code>アノテーションであるフィールドには、コンポーネントが初期化されるときに呼び出したいメソッド名を指定します。メソッドが複数ある時はカンマで区切ることが出来ます。
 * </p>
 * <p>
 * 指定されたメソッド名から初期化メソッドを生成し、コンポーネント定義に追加します。
 * </p>
 * 
 * 
 * <h4><code>DestroyMethod</code>アノテーション</h4>
 * <p>
 * <code>DestroyMethod</code>アノテーションは、フィールド名が<code>DESTROY_METHOD</code>で<code>String</code>型のフィールドです。
 * <code>DestroyMethod</code>アノテーションであるフィールドには、コンポーネントが終了される時に呼び出したいメソッド名を指定します。メソッドが複数ある時はカンマで区切ることが出来ます。
 * </p>
 * <p>
 * 指定されたメソッド名からdestroyメソッドを生成し、コンポーネント定義に追加します。
 * </p>
 * 
 * @author Maeno
 */
public class ConstantAnnotationHandler extends AbstractAnnotationHandler {

    public ComponentDef createComponentDef(Class componentClass,
            InstanceDef defaultInstanceDef,
            AutoBindingDef defaultAutoBindingDef, boolean defaultExternalBinding) {

        String name = null;
        InstanceDef instanceDef = defaultInstanceDef;
        AutoBindingDef autoBindingDef = defaultAutoBindingDef;
        boolean externalBinding = defaultExternalBinding;
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentClass);
        if (!beanDesc.hasField(COMPONENT)) {
            return createComponentDef(componentClass, name, instanceDef,
                    autoBindingDef, externalBinding);
        }
        Field field = beanDesc.getField(COMPONENT);
        if (!isConstantAnnotationField(field)) {
            return createComponentDef(componentClass, name, instanceDef,
                    autoBindingDef, externalBinding);
        }
        String componentStr = (String) FieldUtil.get(field, null);
        String[] array = StringUtil.split(componentStr, "=, ");
        for (int i = 0; i < array.length; i += 2) {
            String key = array[i].trim();
            String value = array[i + 1].trim();
            if (NAME.equalsIgnoreCase(key)) {
                name = value;
            } else if (INSTANCE.equalsIgnoreCase(key)) {
                instanceDef = getInstanceDef(value, defaultInstanceDef);
            } else if (AUTO_BINDING.equalsIgnoreCase(key)) {
                autoBindingDef = getAutoBindingDef(value);
            } else if (EXTERNAL_BINDING.equalsIgnoreCase(key)) {
                externalBinding = Boolean.valueOf(value).booleanValue();
            } else {
                throw new IllegalArgumentException(componentStr);
            }
        }
        return createComponentDef(componentClass, name, instanceDef,
                autoBindingDef, externalBinding);
    }

    /**
     * フィールドが定数アノテーションであるかどうかを判断します。
     * 
     * @param field
     *            フィールド
     * @return フィールドが定数アノテーションの場合は<code>true</code>,そうでない場合は<code>false</code>
     */
    protected boolean isConstantAnnotationField(Field field) {
        final int modifiers = field.getModifiers();
        return Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers)
                && Modifier.isStatic(modifiers);
    }

    public PropertyDef createPropertyDef(BeanDesc beanDesc,
            PropertyDesc propertyDesc) {
        String propName = propertyDesc.getPropertyName();
        String fieldName = propName + BINDING_SUFFIX;
        if (!beanDesc.hasField(fieldName)) {
            return null;
        }
        String bindingStr = (String) beanDesc.getFieldValue(fieldName, null);
        String bindingTypeName = null;
        String expression = null;
        if (bindingStr != null) {
            String[] array = StringUtil.split(bindingStr, "=, ");
            if (array.length == 1) {
                expression = array[0];
            } else {
                for (int i = 0; i < array.length; i += 2) {
                    String key = array[i].trim();
                    String value = array[i + 1].trim();
                    if (BINDING_TYPE.equalsIgnoreCase(key)) {
                        bindingTypeName = value;
                    } else if (VALUE.equalsIgnoreCase(key)) {
                        expression = value;
                    } else {
                        throw new IllegalArgumentException(bindingStr);
                    }
                }
            }
        }
        return createPropertyDef(propName, expression, bindingTypeName,
                AccessTypeDef.PROPERTY_NAME);
    }

    public PropertyDef createPropertyDef(BeanDesc beanDesc, Field field) {
        return null;
    }

    public void appendAspect(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentClass);
        if (!beanDesc.hasField(ASPECT)) {
            return;
        }
        String aspectStr = (String) beanDesc.getFieldValue(ASPECT, null);
        String[] array = StringUtil.split(aspectStr, "=, ");
        String interceptor = null;
        String pointcut = null;
        if (array.length == 1) {
            interceptor = array[0];
        } else {
            for (int i = 0; i < array.length; i += 2) {
                String key = array[i].trim();
                String value = array[i + 1].trim();
                if (VALUE.equalsIgnoreCase(key)) {
                    interceptor = value;
                } else if (POINTCUT.equalsIgnoreCase(key)) {
                    pointcut = value;
                } else {
                    throw new IllegalArgumentException(aspectStr);
                }
            }
        }
        appendAspect(componentDef, interceptor, pointcut);
    }

    /**
     * 指定されたインターセプタ名とポイントカットを表す文字列からアスペクト定義を生成し、コンポーネント定義に追加します。 但し、インターセプタ名が<code>null</code>の場合には{@link EmptyRuntimeException}がスローされます。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param interceptor
     *            インターセプタ名
     * @param pointcut
     *            ポイントカットを表す文字列
     * @throws EmptyRuntimeException
     *             インターセプタ名が<code>null</code>の場合
     */
    protected void appendAspect(ComponentDef componentDef, String interceptor,
            String pointcut) {

        if (interceptor == null) {
            throw new EmptyRuntimeException("interceptor");
        }
        AspectDef aspectDef = AspectDefFactory.createAspectDef(interceptor,
                pointcut);
        componentDef.addAspectDef(aspectDef);
    }

    /**
     * 指定されたインターセプタ名とメソッドからアスペクト定義を生成し、コンポーネント定義に追加します。 但し、インターセプタ名が<code>null</code>の場合には{@link EmptyRuntimeException}がスローされます。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param interceptor
     *            インターセプタ名
     * @param targetMethod
     *            メソッド
     * @throws EmptyRuntimeException
     *             インターセプタ名が<code>null</code>の場合
     */
    protected void appendAspect(ComponentDef componentDef, String interceptor,
            Method targetMethod) {

        if (interceptor == null) {
            throw new EmptyRuntimeException("interceptor");
        }
        AspectDef aspectDef = AspectDefFactory.createAspectDef(interceptor,
                targetMethod);
        componentDef.addAspectDef(aspectDef);
    }

    public void appendInterType(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentClass);
        if (!beanDesc.hasField(INTER_TYPE)) {
            return;
        }
        String interTypeStr = (String) beanDesc.getFieldValue(INTER_TYPE, null);
        String[] array = StringUtil.split(interTypeStr, ", ");
        for (int i = 0; i < array.length; i += 2) {
            String interTypeName = array[i].trim();
            appendInterType(componentDef, interTypeName);
        }
    }

    /**
     * インタータイプ定義を生成し、その定義にインタータイプ名で生成したOGNL式をセットしてコンポーネント定義に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param interTypeName
     *            インタータイプ名
     */
    protected void appendInterType(ComponentDef componentDef,
            String interTypeName) {
        InterTypeDef interTypeDef = new InterTypeDefImpl();
        interTypeDef.setExpression(new OgnlExpression(interTypeName));
        componentDef.addInterTypeDef(interTypeDef);
    }

    public void appendInitMethod(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentClass);
        if (!beanDesc.hasField(INIT_METHOD)) {
            return;
        }
        String initMethodStr = (String) beanDesc.getFieldValue(INIT_METHOD,
                null);
        if (StringUtil.isEmpty(initMethodStr)) {
            return;
        }
        String[] array = StringUtil.split(initMethodStr, ", ");
        for (int i = 0; i < array.length; ++i) {
            String methodName = array[i].trim();
            if (!beanDesc.hasMethod(methodName)) {
                throw new IllegalInitMethodAnnotationRuntimeException(
                        componentClass, methodName);
            }
            Method[] methods = beanDesc.getMethods(methodName);
            if (methods.length != 1
                    || methods[0].getParameterTypes().length != 0) {
                throw new IllegalInitMethodAnnotationRuntimeException(
                        componentClass, methodName);
            }
            if (!isInitMethodRegisterable(componentDef, methodName)) {
                continue;
            }
            appendInitMethod(componentDef, methodName);
        }
    }

    /**
     * 指定されたメソッドから初期化メソッド定義を生成し、コンポーネント定義に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param method
     *            メソッド
     */
    protected void appendInitMethod(ComponentDef componentDef, Method method) {
        InitMethodDef initMethodDef = new InitMethodDefImpl(method);
        componentDef.addInitMethodDef(initMethodDef);
    }

    /**
     * 指定されたメソッド名から初期化メソッド定義を生成し、コンポーネント定義に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param methodName
     *            メソッド名
     */
    protected void appendInitMethod(ComponentDef componentDef, String methodName) {
        InitMethodDef initMethodDef = new InitMethodDefImpl(methodName);
        componentDef.addInitMethodDef(initMethodDef);
    }

    public void appendDestroyMethod(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentClass);
        if (!beanDesc.hasField(DESTROY_METHOD)) {
            return;
        }
        String destroyMethodStr = (String) beanDesc.getFieldValue(
                DESTROY_METHOD, null);
        if (StringUtil.isEmpty(destroyMethodStr)) {
            return;
        }
        String[] array = StringUtil.split(destroyMethodStr, ", ");
        for (int i = 0; i < array.length; ++i) {
            String methodName = array[i].trim();
            if (!beanDesc.hasMethod(methodName)) {
                throw new IllegalDestroyMethodAnnotationRuntimeException(
                        componentClass, methodName);
            }
            Method[] methods = beanDesc.getMethods(methodName);
            if (methods.length != 1
                    || methods[0].getParameterTypes().length != 0) {
                throw new IllegalDestroyMethodAnnotationRuntimeException(
                        componentClass, methodName);
            }
            if (!isDestroyMethodRegisterable(componentDef, methodName)) {
                continue;
            }
            appendDestroyMethod(componentDef, methodName);
        }
    }

    /**
     * 指定されたメソッドからdestroyメソッド定義を生成し、コンポーネント定義に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param targetMethod
     *            メソッド
     */
    protected void appendDestroyMethod(ComponentDef componentDef,
            Method targetMethod) {
        DestroyMethodDef destroyMethodDef = new DestroyMethodDefImpl(
                targetMethod);
        componentDef.addDestroyMethodDef(destroyMethodDef);
    }

    /**
     * 指定されたメソッド名からdestroyメソッド定義を生成し、コンポーネント定義に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param methodName
     *            メソッド名
     */
    protected void appendDestroyMethod(ComponentDef componentDef,
            String methodName) {
        DestroyMethodDef destroyMethodDef = new DestroyMethodDefImpl(methodName);
        componentDef.addDestroyMethodDef(destroyMethodDef);
    }
}
