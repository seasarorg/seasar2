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
package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 1つのキーに複数のコンポーネントが登録されていた場合にスローされます。
 * <p>
 * S2コンテナからコンポーネントを取得しようとした際に、 指定したキー(コンポーネントのクラス、 インターフェース、
 * あるいは名前)に該当するコンポーネント定義が複数存在した場合、 この例外が発生します。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see org.seasar.framework.container.impl.TooManyRegistrationComponentDefImpl#getComponent()
 */
public class TooManyRegistrationRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = -6522677955855595193L;

    private Object key_;

    private Class[] componentClasses_;

    /**
     * <code>TooManyRegistrationRuntimeException</code>を構築します。
     * 
     * @param key
     *            コンポーネントを取得しようとした際に使用したキー
     * @param componentClasses
     *            1つのキーに登録された複数コンポーネントのクラスの配列
     */
    public TooManyRegistrationRuntimeException(Object key,
            Class[] componentClasses) {
        super("ESSR0045", new Object[] { key, getClassNames(componentClasses) });
        key_ = key;
        componentClasses_ = componentClasses;
    }

    /**
     * コンポーネントを取得しようとした際に使用したキーを返します。
     * 
     * @return コンポーネントを取得するためのキー
     */
    public Object getKey() {
        return key_;
    }

    /**
     * 1つのキーに登録された複数コンポーネントのクラスの配列を返します。
     * 
     * @return コンポーネントのクラスの配列
     */
    public Class[] getComponentClasses() {
        return componentClasses_;
    }

    private static String getClassNames(Class[] componentClasses) {
        StringBuffer buf = new StringBuffer(255);
        for (int i = 0; i < componentClasses.length; ++i) {
            if (componentClasses[i] != null) {
                buf.append(componentClasses[i].getName());
            } else {
                buf.append("<unknown>");
            }
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        return buf.toString();
    }
}