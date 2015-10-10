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
package org.seasar.extension.sql.context;

import java.util.ArrayList;
import java.util.List;

import ognl.OgnlRuntime;

import org.seasar.extension.sql.SqlContext;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * {@link SqlContext}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class SqlContextImpl implements SqlContext {

    private static Logger logger = Logger.getLogger(SqlContextImpl.class);

    private CaseInsensitiveMap args = new CaseInsensitiveMap();

    private CaseInsensitiveMap argTypes = new CaseInsensitiveMap();

    private StringBuffer sqlBuf = new StringBuffer(255);

    private List bindVariables = new ArrayList();

    private List bindVariableTypes = new ArrayList();

    private boolean enabled = true;

    private SqlContext parent;

    static {
        OgnlRuntime.setPropertyAccessor(SqlContext.class,
                new SqlContextPropertyAccessor());
    }

    /**
     * <code>SqlContextImpl</code>を作成します。
     */
    public SqlContextImpl() {
    }

    /**
     * <code>SqlContextImpl</code>を作成します。
     * 
     * @param parent
     */
    public SqlContextImpl(SqlContext parent) {
        this.parent = parent;
        enabled = false;
    }

    public Object getArg(String name) {
        if (args.containsKey(name)) {
            return args.get(name);
        } else if (parent != null) {
            return parent.getArg(name);
        } else {
            if (args.size() == 1) {
                return args.get(0);
            }
            logger.log("WSSR0010", new Object[] { name });
            return null;
        }
    }

    public boolean hasArg(String name) {
        if (args.containsKey(name)) {
            return true;
        } else if (parent != null) {
            return parent.hasArg(name);
        }
        return false;
    }

    public Class getArgType(String name) {
        if (argTypes.containsKey(name)) {
            return (Class) argTypes.get(name);
        } else if (parent != null) {
            return parent.getArgType(name);
        } else {
            if (argTypes.size() == 1) {
                return (Class) argTypes.get(0);
            }
            logger.log("WSSR0010", new Object[] { name });
            return null;
        }
    }

    public void addArg(String name, Object arg, Class argType) {
        args.put(name, arg);
        argTypes.put(name, argType);
    }

    public String getSql() {
        return sqlBuf.toString();
    }

    public Object[] getBindVariables() {
        return bindVariables.toArray(new Object[bindVariables.size()]);
    }

    public Class[] getBindVariableTypes() {
        return (Class[]) bindVariableTypes.toArray(new Class[bindVariableTypes
                .size()]);
    }

    public SqlContext addSql(String sql) {
        sqlBuf.append(sql);
        return this;
    }

    public SqlContext addSql(String sql, Object bindVariable,
            Class bindVariableType) {

        sqlBuf.append(sql);
        bindVariables.add(bindVariable);
        bindVariableTypes.add(bindVariableType);
        return this;
    }

    public SqlContext addSql(String sql, Object[] bindVariables,
            Class[] bindVariableTypes) {

        sqlBuf.append(sql);
        for (int i = 0; i < bindVariables.length; ++i) {
            this.bindVariables.add(bindVariables[i]);
            this.bindVariableTypes.add(bindVariableTypes[i]);
        }
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}