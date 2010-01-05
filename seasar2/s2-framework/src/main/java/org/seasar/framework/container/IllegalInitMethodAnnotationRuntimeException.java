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
package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

/**
 * アノテーションで指定された{@link InitMethodDef initメソッド・インジェクション定義}が不正だった場合にスローされます。
 * <p>
 * アノテーションで指定されたメソッドが存在しない場合、 複数定義されている場合、 および引数が必要な場合に不正とみなされます。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see org.seasar.framework.container.factory.ConstantAnnotationHandler
 */
public class IllegalInitMethodAnnotationRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 0L;

    private Class componentClass_;

    private String methodName_;

    /**
     * <code>IllegalInitMethodAnnotationRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            アノテーションが指定されたクラス
     * @param methodName
     *            アノテーションで指定されたメソッド名
     */
    public IllegalInitMethodAnnotationRuntimeException(Class componentClass,
            String methodName) {
        super("ESSR0081", new Object[] { componentClass.getName(), methodName });
        componentClass_ = componentClass;
        methodName_ = methodName;
    }

    /**
     * 例外の原因となったアノテーションが指定されたクラスを返します。
     * 
     * @return アノテーションが指定されたクラス
     */
    public Class getComponentClass() {
        return componentClass_;
    }

    /**
     * 例外の原因となったアノテーションで指定されたメソッド名を返します。
     * 
     * @return アノテーションで指定されたメソッド名
     */
    public String getMethodName() {
        return methodName_;
    }
}