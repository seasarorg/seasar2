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

import java.util.Arrays;
import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public abstract class ComposableWhere implements Where {

    protected List<Where> children = CollectionsUtil.newArrayList();

    protected ComposableWhereContext context;

    public ComposableWhere(final Where... children) {
        this.children.addAll(Arrays.asList(children));
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

    protected ComposableWhere addChildren(Where... children) {
        this.children.addAll(Arrays.asList(children));
        return this;
    }

    protected abstract void visit(ComposableWhereContext context);

}
