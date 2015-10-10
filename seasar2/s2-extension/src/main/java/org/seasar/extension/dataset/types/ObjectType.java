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
package org.seasar.extension.dataset.types;

import org.seasar.extension.dataset.ColumnType;

/**
 * 汎用的なオブジェクト用の {@link ColumnType}です。
 * 
 * @author higa
 * 
 */
public class ObjectType implements ColumnType {

    ObjectType() {
    }

    public Object convert(Object value, String formatPattern) {
        return value;
    }

    public boolean equals(Object arg1, Object arg2) {
        if (arg1 == null) {
            return arg2 == null;
        }
        return doEquals(arg1, arg2);
    }

    /**
     * 等しいかどうかを返します。
     * 
     * @param arg1
     *            引数1
     * @param arg2
     *            引数1
     * @return 等しいかどうか
     */
    protected boolean doEquals(Object arg1, Object arg2) {
        try {
            arg1 = convert(arg1, null);
        } catch (Throwable t) {
            return false;
        }
        try {
            arg2 = convert(arg2, null);
        } catch (Throwable t) {
            return false;
        }
        if ((arg1 instanceof Comparable) && (arg2 instanceof Comparable)) {
            return ((Comparable) arg1).compareTo(arg2) == 0;
        }
        return arg1.equals(arg2);
    }

    public Class getType() {
        return Object.class;
    }
}
