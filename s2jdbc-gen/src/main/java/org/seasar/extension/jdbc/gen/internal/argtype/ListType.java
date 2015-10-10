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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link List}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            リストの要素の型
 */
public class ListType<T> extends CollectionType<T> {

    /**
     * インスタンスを構築します。
     * 
     * @param argumentType
     *            引数の型
     */
    public ListType(ArgumentType<T> argumentType) {
        super(argumentType);
    }

    @Override
    protected Collection<T> createCollection() {
        return new ArrayList<T>();
    }

}
