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
package org.seasar.extension.jdbc.name;

import org.seasar.framework.util.StringUtil;

/**
 * プロパティ名をあらわすクラスです。ネストしたプロパティに対応しています。
 * 
 * @author higa
 * @param <T>
 *            プロパティの型です。
 */
public class PropertyName<T> implements CharSequence {

    /**
     * プロパティの名前です。
     */
    protected final String name;

    /**
     * コンストラクタです。
     */
    public PropertyName() {
        this(null, null);
    }

    /**
     * コンストラクタです。
     * 
     * @param name
     *            名前
     */
    public PropertyName(final String name) {
        this(null, name);
    }

    /**
     * コンストラクタです。
     * 
     * @param parent
     *            親
     * @param name
     *            名前
     */
    public PropertyName(final PropertyName<?> parent, final String name) {
        if (StringUtil.isEmpty(name)) {
            this.name = "";
            return;
        }
        if (parent == null || parent.length() == 0) {
            this.name = name;
            return;
        }
        this.name = parent + "." + name;
    }

    public char charAt(final int index) {
        return name.charAt(index);
    }

    public int length() {
        return name.length();
    }

    public CharSequence subSequence(final int start, final int end) {
        return name.substring(start, end);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return name.equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}