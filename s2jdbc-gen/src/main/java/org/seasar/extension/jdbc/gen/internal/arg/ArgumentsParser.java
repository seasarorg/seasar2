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

import org.seasar.extension.jdbc.gen.internal.argtype.ArgumentType;
import org.seasar.extension.jdbc.gen.internal.argtype.ArgumentTypeRegistry;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.log.Logger;

/**
 * コマンドラインの引数を解析するクラスです。
 * <p>
 * それぞれの引数は「=」で連結されたキーと値の組である必要があります。 このクラスは、キーに対応するJavaBeanのプロパティに値を設定します。
 * </p>
 * 
 * @author taedium
 */
public class ArgumentsParser {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(ArgumentsParser.class);

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
    public ArgumentsParser(Object bean) {
        if (bean == null) {
            throw new NullPointerException("bean");
        }
        this.bean = bean;
        beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
    }

    /**
     * 解析します。
     * 
     * @param args
     */
    public void parse(String[] args) {
        for (String arg : args) {
            int pos = arg.indexOf("=");
            if (pos < 0) {
                throw new IllegalArgumentException(arg);
            }
            String key = arg.substring(0, pos);
            String value = arg.substring(pos + 1, arg.length());
            if (!beanDesc.hasPropertyDesc(key)) {
                continue;
            }
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(key);
            if (!propertyDesc.hasWriteMethod()) {
                continue;
            }
            ArgumentType<?> argumentType = ArgumentTypeRegistry
                    .getArgumentType(propertyDesc);
            if (argumentType == null) {
                logger.log("WS2JDBCGEN0001", new Object[] {
                        bean.getClass().getName(),
                        propertyDesc.getPropertyName() });
                continue;
            }
            Object propertyValue = argumentType.toObject(value);
            propertyDesc.setValue(bean, propertyValue);
        }
    }

}
