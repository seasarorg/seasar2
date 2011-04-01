/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
import java.lang.reflect.Modifier;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * AnnotationHandlerの抽象クラスです。
 * <p>
 * このクラスでは、主にクラス、メソッド、フィールドに書かれたアノーテションを取得します。
 * アノーテションから、コンポーネントに対しての各定義の作成はサブクラスで行います。
 * </p>
 * 
 * @author vestige
 */
public abstract class AbstractAnnotationHandler implements AnnotationHandler {

    /**
     * COMPONENTアノテーションを表す定数名です。
     */
    protected static final String COMPONENT = "COMPONENT";

    /**
     * COMPONENTアノテーションのname属性「コンポーネント名」を表すキーです。
     */
    protected static final String NAME = "name";

    /**
     * COMPONENTアノテーションのinstance属性「インスタンス定義」を表すキーです。
     */
    protected static final String INSTANCE = "instance";

    /**
     * COMPONENTアノテーションのautoBinding属性「自動バインディング定義」を表すキーです。
     */
    protected static final String AUTO_BINDING = "autoBinding";

    /**
     * Bindingアノテーションとして識別するための定数です。
     */
    protected static final String BINDING_SUFFIX = "_BINDING";

    /**
     * BindingアノテーションのbindingType属性「バインディングタイプ」を表すキーです。
     */
    protected static final String BINDING_TYPE = "bindingType";

    /**
     * COMPONENTアノテーションのexternalBinding属性「外部バインディング定義」を表すキーです。
     */
    protected static final String EXTERNAL_BINDING = "externalBinding";

    /**
     * 各アノテーション内のvalue属性「VALUE」を表すキーです。
     */
    protected static final String VALUE = "value";

    /**
     * ASPECTアノテーションを表す定数名です。
     */
    protected static final String ASPECT = "ASPECT";

    /**
     * InterTypeアノテーションを表す定数名です。
     */
    protected static final String INTER_TYPE = "INTER_TYPE";

    /**
     * InitMethodアノテーションを表す定数名です。
     */
    protected static final String INIT_METHOD = "INIT_METHOD";

    /**
     * DestroyMethodアノテーションを表す定数名です。
     */
    protected static final String DESTROY_METHOD = "DESTROY_METHOD";

    /**
     * ASPECTアノテーションのinterceptor属性「インターセプター定義」を表すキーです。
     */
    protected static final String INTERCEPTOR = "interceptor";

    /**
     * ASPECTアノテーションのpointcut属性「ポイントカット定義」を表すキーです。
     */
    protected static final String POINTCUT = "pointcut";

    public ComponentDef createComponentDef(String className,
            InstanceDef instanceDef) {
        return createComponentDef(ClassUtil.forName(className), instanceDef);
    }

    public ComponentDef createComponentDef(String className,
            InstanceDef instanceDef, AutoBindingDef autoBindingDef) {
        return createComponentDef(ClassUtil.forName(className), instanceDef,
                autoBindingDef);
    }

    public ComponentDef createComponentDef(String className,
            InstanceDef instanceDef, AutoBindingDef autoBindingDef,
            boolean externalBinding) {
        return createComponentDef(ClassUtil.forName(className), instanceDef,
                autoBindingDef, externalBinding);
    }

    public ComponentDef createComponentDef(Class componentClass,
            InstanceDef instanceDef) {
        return createComponentDef(componentClass, instanceDef, null);
    }

    public ComponentDef createComponentDef(Class componentClass,
            InstanceDef instanceDef, AutoBindingDef autoBindingDef) {
        return createComponentDef(componentClass, instanceDef, autoBindingDef,
                false);
    }

    public void appendDI(ComponentDef componentDef) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(componentDef
                .getComponentClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.isWritable()) {
                continue;
            }
            PropertyDef propDef = createPropertyDef(beanDesc, pd);
            if (propDef == null) {
                continue;
            }
            componentDef.addPropertyDef(propDef);
        }
        for (int i = 0; i < beanDesc.getFieldSize(); ++i) {
            Field field = beanDesc.getField(i);
            if (componentDef.hasPropertyDef(field.getName())) {
                continue;
            }
            if (!isFieldInjectionTarget(field)) {
                continue;
            }
            PropertyDef propDef = createPropertyDef(beanDesc, field);
            if (propDef == null) {
                continue;
            }
            componentDef.addPropertyDef(propDef);
        }
    }

    /**
     * インスタンス定義を返します。
     * <p>
     * 指定した名前のインスタンス定義が存在しない場合、デフォルトのインスタンス定義を返します。
     * </p>
     * 
     * @param name
     *            インスタンス定義の種類
     * @param defaultInstanceDef
     *            デフォルトのインスタンス定義（singleton）
     * @return インスタンス定義
     */
    protected InstanceDef getInstanceDef(String name,
            InstanceDef defaultInstanceDef) {
        InstanceDef instanceDef = getInstanceDef(name);
        if (instanceDef != null) {
            return instanceDef;
        }
        return defaultInstanceDef;
    }

    /**
     * インスタンス定義を返します。
     * 
     * @param name
     *            インスタンス定義の種類
     * @return インスタンス定義
     */
    protected InstanceDef getInstanceDef(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        return InstanceDefFactory.getInstanceDef(name);
    }

    /**
     * 自動バインディング定義を返します。
     * 
     * @param name
     *            自動バインディング定義の種類
     * @return 自動バインディング定義
     */
    protected AutoBindingDef getAutoBindingDef(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        return AutoBindingDefFactory.getAutoBindingDef(name);
    }

    /**
     * 指定したコンポーネントクラスからコンポーネント定義を作成します。
     * <p>
     * 以下の設定がある場合はおのおのコンポーネント定義に設定します。
     * <ul>
     * <li>コンポーネント名
     * <li>インスタンス定義
     * <li>自動バインディング定義
     * <li>外部バインディングの有無
     * </ul>
     * </p>
     * 
     * @param componentClass
     *            コンポーネントクラス
     * @param name
     *            コンポーネント名
     * @param instanceDef
     *            インスタンス定義
     * @param autoBindingDef
     *            自動バインディング定義
     * @param externalBinding
     *            外部バインディングの有無
     * @return コンポーネント定義
     */
    protected ComponentDef createComponentDef(Class componentClass,
            String name, InstanceDef instanceDef,
            AutoBindingDef autoBindingDef, boolean externalBinding) {
        ComponentDef componentDef = new ComponentDefImpl(componentClass);
        if (!StringUtil.isEmpty(name)) {
            componentDef.setComponentName(name);
        }
        if (instanceDef != null) {
            componentDef.setInstanceDef(instanceDef);
        }
        if (autoBindingDef != null) {
            componentDef.setAutoBindingDef(autoBindingDef);
        }
        componentDef.setExternalBinding(externalBinding);
        return componentDef;
    }

    /**
     * プロパティ定義を作成します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param expression
     *            引数定義の値となる式
     * @param bindingTypeName
     *            バインディングタイプ定義
     * @param accessTypeName
     *            アクセスタイプ定義
     * @return プロパティ定義
     */
    protected PropertyDef createPropertyDef(String propertyName,
            String expression, String bindingTypeName, String accessTypeName) {
        PropertyDef propertyDef = new PropertyDefImpl(propertyName);
        if (!StringUtil.isEmpty(bindingTypeName)) {
            BindingTypeDef bindingTypeDef = BindingTypeDefFactory
                    .getBindingTypeDef(bindingTypeName);
            propertyDef.setBindingTypeDef(bindingTypeDef);
        }
        if (!StringUtil.isEmpty(accessTypeName)) {
            AccessTypeDef accessTypeDef = AccessTypeDefFactory
                    .getAccessTypeDef(accessTypeName);
            propertyDef.setAccessTypeDef(accessTypeDef);
        }
        if (!StringUtil.isEmpty(expression)) {
            propertyDef.setExpression(new OgnlExpression(expression));
        }
        return propertyDef;
    }

    public boolean isInitMethodRegisterable(ComponentDef cd, String methodName) {
        if (StringUtil.isEmpty(methodName)) {
            return false;
        }
        for (int i = 0; i < cd.getInitMethodDefSize(); ++i) {
            InitMethodDef other = cd.getInitMethodDef(i);
            if (methodName.equals(other.getMethodName())
                    && other.getArgDefSize() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isDestroyMethodRegisterable(ComponentDef cd,
            String methodName) {
        if (StringUtil.isEmpty(methodName)) {
            return false;
        }
        for (int i = 0; i < cd.getDestroyMethodDefSize(); ++i) {
            DestroyMethodDef other = cd.getDestroyMethodDef(i);
            if (methodName.equals(other.getMethodName())
                    && other.getArgDefSize() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 指定したフィールドがインジェクション可能かどうかの判定を行います。
     * <P>
     * <code>static</code>または、<code>final</code>でない場合はインジェクションが可能になります。
     * </p>
     * 
     * @param field
     *            フィールド
     * @return フィールドが<code>static</code>、<code>final</code>でない場合は<code>true</code>、そうでない場合は<code>false</code>を返す。
     */
    protected boolean isFieldInjectionTarget(Field field) {
        return !Modifier.isStatic(field.getModifiers())
                && !Modifier.isFinal(field.getModifiers());
    }
}