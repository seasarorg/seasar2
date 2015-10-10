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
package org.seasar.extension.jdbc.gen.internal.util;

import java.lang.reflect.Field;

import org.seasar.framework.env.Env;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link Env}に関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class EnvUtil {

    /** 環境名と環境名以外の区切り文字 */
    public static char DELIMITER = '#';

    /** {@link Env}の環境名を保持するフィールド */
    protected static Field VALUE_FIELD;
    static {
        VALUE_FIELD = ReflectionUtil.getDeclaredField(Env.class, "value");
        VALUE_FIELD.setAccessible(true);
    }

    /**
     * 
     */
    protected EnvUtil() {
    }

    /**
     * 環境名を返します。
     * 
     * @return 環境名
     */
    public static String getValue() {
        return ReflectionUtil.getStaticValue(VALUE_FIELD);
    }

    /**
     * 環境名を設定します。
     * 
     * @param value
     *            環境名
     */
    public static void setValue(String value) {
        ReflectionUtil.setStaticValue(VALUE_FIELD, value);
    }
}
