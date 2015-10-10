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
package org.seasar.extension.dao.helper.impl;

import java.lang.reflect.Method;

import org.seasar.extension.dao.DaoConstants;
import org.seasar.extension.dao.DaoNotFoundRuntimeException;
import org.seasar.extension.dao.helper.DaoHelper;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TextUtil;

/**
 * {@link DaoHelper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class DaoHelperImpl implements DaoHelper {

    private NamingConvention namingConvention;

    /**
     * @return 命名規約を返します。
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * 命名規約を設定します。
     * 
     * @param namingConvention
     *            命名規約
     */
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public Class getDaoInterface(Class clazz) {
        if (clazz.isInterface()) {
            return clazz;
        }
        for (Class target = clazz; target != Object.class; target = target
                .getSuperclass()) {
            Class[] interfaces = target.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                Class intf = interfaces[i];
                if (intf.getName().endsWith(namingConvention.getDaoSuffix())) {
                    return intf;
                }
            }
        }
        throw new DaoNotFoundRuntimeException(clazz);
    }

    public String getDataSourceName(Class daoClass) {
        Class intf = getDaoInterface(daoClass);
        String className = intf.getName();
        String key = "." + namingConvention.getDaoPackageName() + ".";
        int index = className.lastIndexOf(key);
        if (index < 0) {
            return null;
        }
        int index2 = className.lastIndexOf('.');
        if (index + key.length() - 1 == index2) {
            return null;
        }
        return className.substring(index + key.length(), index2);
    }

    public String getSqlBySqlFile(Class daoClass, Method method, String suffix) {
        String base = daoClass.getName().replace('.', '/') + "_"
                + method.getName();
        String dbmsPath = base
                + (StringUtil.isEmpty(suffix) ? "" : "_" + suffix)
                + DaoConstants.SQL_EXTENSION;
        String standardPath = base + DaoConstants.SQL_EXTENSION;
        if (ResourceUtil.isExist(dbmsPath)) {
            return TextUtil.readUTF8(dbmsPath);
        } else if (ResourceUtil.isExist(standardPath)) {
            return TextUtil.readUTF8(standardPath);
        }
        return null;
    }
}