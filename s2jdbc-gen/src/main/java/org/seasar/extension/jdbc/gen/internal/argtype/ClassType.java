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
package org.seasar.extension.jdbc.gen.internal.argtype;

import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link Class}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            クラスの型
 */
public class ClassType implements ArgumentType<Class<?>> {

    public Class<?> toObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return ReflectionUtil.forName(value);
    }

    public String toText(Class<?> value) {
        if (value == null) {
            return "";
        }
        return value.getName();
    }

}
