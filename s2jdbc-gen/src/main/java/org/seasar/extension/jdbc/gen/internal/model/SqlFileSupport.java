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
package org.seasar.extension.jdbc.gen.internal.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * SQLファイルのサポートクラスです。
 * 
 * @author taedium
 * 
 */
public class SqlFileSupport {

    /**
     * SQLファイルのパスのリストを作成します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @return SQLファイルのパスのリスト
     */
    public List<String> createSqlFilePathList(File classpathDir,
            Set<File> sqlFileSet) {
        List<String> sqlFilePathList = new ArrayList<String>();
        Set<String> dbmsNameSet = getDbmsNameSet();
        String basePath = FileUtil.getCanonicalPath(classpathDir)
                + File.separator;

        for (File sqlFile : sqlFileSet) {
            String path = FileUtil.getCanonicalPath(sqlFile);
            if (!path.startsWith(basePath)) {
                continue;
            }
            path = path.substring(basePath.length());
            if (path.endsWith(".sql")) {
                path = path.substring(0, path.length() - 4);
            }
            for (String dbmsName : dbmsNameSet) {
                if (path.endsWith("_" + dbmsName)) {
                    path = path.substring(0, path.length() - dbmsName.length()
                            - 1);
                    break;
                }
            }
            String resourcePath = path.replace(File.separator, "/") + ".sql";
            if (sqlFilePathList.contains(resourcePath)) {
                continue;
            }
            sqlFilePathList.add(resourcePath);
        }

        Collections.sort(sqlFilePathList);
        return sqlFilePathList;
    }

    /**
     * コンテナに登録されているすべての{@link DbmsDialect}について名前のセットを返します。
     * 
     * @return {@link DbmsDialect}の名前のセット
     */
    protected Set<String> getDbmsNameSet() {
        if (!SingletonS2ContainerFactory.hasContainer()) {
            return Collections.emptySet();
        }
        Set<String> dbmsNameSet = new HashSet<String>();
        S2Container container = SingletonS2ContainerFactory.getContainer();
        DbmsDialect[] dialects = (DbmsDialect[]) container
                .findAllComponents(DbmsDialect.class);
        for (DbmsDialect dialect : dialects) {
            String name = dialect.getName();
            if (name != null) {
                dbmsNameSet.add(name);
            }
        }
        return dbmsNameSet;
    }

}
