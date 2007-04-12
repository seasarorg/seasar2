/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence.mapper;

import org.seasar.extension.persistence.ColumnMapper;
import org.seasar.extension.persistence.RowMapper;
import org.seasar.framework.util.ClassUtil;

/**
 * 行をBeanにマッピングするクラスです。
 * 
 * @author higa
 * 
 */
public class BeanRowMapper implements RowMapper {

    private Class<?> beanClass;

    private ColumnMapper[] columnMappers;

    private Object target;

    /**
     * <code>BeanRowMapper</code>を作成します。
     * 
     * @param beanClass
     * @param columnMappers
     */
    public BeanRowMapper(Class<?> beanClass, ColumnMapper[] columnMappers) {
        this.beanClass = beanClass;
        this.columnMappers = columnMappers;
    }

    public void enterRow() {
        target = ClassUtil.newInstance(beanClass);
    }

    public Object leaveRow() {
        return target;
    }

    public void setValue(int index, Object value) {
        columnMappers[index].setValue(target, value);
    }

}
