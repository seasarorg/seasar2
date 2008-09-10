/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.where;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.ArrayUtil;

/**
 * ANDをあらわすクラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractAndOr implements Where {

    /**
     * ANDやORといった表現です。
     */
    protected String expression;

    /**
     * 子供たちです。
     */
    protected Where[] children = new Where[0];

    /**
     * コンストラクタです。
     * 
     * @param expression
     *            ANDやORといった表現
     * @param children
     *            子供たちです。
     */
    public AbstractAndOr(String expression, Where... children) {
        this.expression = expression;
        this.children = children;
    }

    public String getCriteria() {
        if (children.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(50);
        sb.append("(");
        for (int i = 0; i < children.length; i++) {
            if (i > 0) {
                sb.append(" " + expression + " ");
            }
            sb.append(children[i].getCriteria());
        }
        sb.append(")");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public Object[] getParams() {
        List<Object> params = new ArrayList<Object>();
        for (int i = 0; i < children.length; i++) {
            params.addAll(ArrayUtil.toList(children[i].getParams()));
        }
        return params.toArray();
    }

    @SuppressWarnings("unchecked")
    public String[] getPropertyNames() {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < children.length; i++) {
            names.addAll(ArrayUtil.toList(children[i].getPropertyNames()));
        }
        return names.toArray(new String[names.size()]);
    }
}
