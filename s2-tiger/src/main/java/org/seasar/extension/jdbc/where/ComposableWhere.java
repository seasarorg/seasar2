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
package org.seasar.extension.jdbc.where;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 複数の構成要素からなる条件を組み立てるための抽象クラスです。
 * 
 * @author koichik
 */
public abstract class ComposableWhere implements Where {

    /** 条件に追加される子供の条件です。 */
    protected List<Where> children = CollectionsUtil.newArrayList();

    /** 条件を組み立てるためのコンテキストです。 */
    protected ComposableWhereContext context;

    /**
     * インスタンスを構築します。
     * 
     * @param children
     *            子供の条件
     */
    public ComposableWhere(final Where... children) {
        this(Arrays.asList(children));
    }

    /**
     * インスタンスを構築します。
     * 
     * @param children
     *            子供の条件
     */
    public ComposableWhere(final Collection<Where> children) {
        this.children.addAll(children);
    }

    public String getCriteria() {
        if (context == null) {
            context = new ComposableWhereContext();
            visit(context);
        }
        return context.getCriteria();
    }

    public Object[] getParams() {
        return context.getParams();
    }

    public String[] getPropertyNames() {
        return context.getPropertyNames();
    }

    /**
     * 子供の条件を追加します。
     * 
     * @param children
     *            子供の条件
     * @return このインスタンス自身
     */
    protected ComposableWhere addChildren(final Where... children) {
        this.children.addAll(Arrays.asList(children));
        return this;
    }

    /**
     * コンテキストに対して条件を組み立てます。
     * 
     * @param context
     *            条件を組み立てるコンテキスト
     */
    protected abstract void visit(ComposableWhereContext context);

}
