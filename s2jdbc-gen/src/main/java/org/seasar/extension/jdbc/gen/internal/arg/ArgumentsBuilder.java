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
package org.seasar.extension.jdbc.gen.internal.arg;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.internal.argtype.ArgumentType;
import org.seasar.extension.jdbc.gen.internal.argtype.ArgumentTypeRegistry;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.log.Logger;

/**
 * コマンドライン用の引数を組み立てるクラスです。
 * <p>
 * JavaBeanのプロパティと値の組を「=」で連結し1つの引数とします。
 * </p>
 * 
 * @author taedium
 */
public class ArgumentsBuilder {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(ArgumentsBuilder.class);

    /** JavaBeanのインスタンス */
    protected Object bean;

    /** Bean記述 */
    protected BeanDesc beanDesc;

    /**
     * インスタンスを構築します。
     * 
     * @param bean
     *            JavaBeanのインスタンス
     */
    public ArgumentsBuilder(Object bean) {
        if (bean == null) {
            throw new NullPointerException("bean");
        }
        this.bean = bean;
        beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
    }

    /**
     * 引数を組み立てます。
     * 
     * @return 引数のリスト
     */
    public List<String> build() {
        ArrayList<String> args = new ArrayList<String>();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (!propertyDesc.hasReadMethod()) {
                continue;
            }
            ArgumentType<Object> argumentType = ArgumentTypeRegistry
                    .getArgumentType(propertyDesc);
            if (argumentType == null) {
                String message = String
                        .format(
                                "No ArgumentType for the property(%s) of class(%s). Process skipped.",
                                propertyDesc.getPropertyName(), bean.getClass()
                                        .getName());
                logger.warn(message);
                continue;
            }
            String name = propertyDesc.getPropertyName();
            Object value = propertyDesc.getValue(bean);
            args.add(name + "=" + argumentType.toText(value));
        }
        return args;
    }
}
