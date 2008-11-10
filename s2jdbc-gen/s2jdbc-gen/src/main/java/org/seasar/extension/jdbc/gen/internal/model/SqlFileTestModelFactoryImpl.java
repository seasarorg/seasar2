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
package org.seasar.extension.jdbc.gen.internal.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModelFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * {@link SqlFileTestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlFileTestModelFactoryImpl implements SqlFileTestModelFactory {

    /** 設定ファイルのパス */
    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** パッケージ名、パッケージ名を指定しない場合は{@code null} */
    protected String packageName;

    /** テストクラスの単純名 */
    protected String shortClassName;

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
    protected boolean useS2junit4;

    /** SQLファイルのパスのリスト */
    protected List<String> sqlFilePathList;

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @param configPath
     *            設定ファイルのパス
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            テストクラスの単純名
     * @param useS2junit4
     *            S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
    public SqlFileTestModelFactoryImpl(File classpathDir, Set<File> sqlFileSet,
            String configPath, String jdbcManagerName, String packageName,
            String shortClassName, boolean useS2junit4) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (sqlFileSet == null) {
            throw new NullPointerException("sqlFileSet");
        }
        if (configPath == null) {
            throw new NullPointerException("configPath");
        }
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (shortClassName == null) {
            throw new NullPointerException("shortClassName");
        }
        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.packageName = packageName;
        this.shortClassName = shortClassName;
        this.useS2junit4 = useS2junit4;
        this.sqlFilePathList = createSqlFilePathList(classpathDir, sqlFileSet);
    }

    /**
     * SQLファイルのパスのリストを作成します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @return SQLファイルのパスのリスト
     */
    protected List<String> createSqlFilePathList(File classpathDir,
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

    public SqlFileTestModel getSqlFileTestModel() {
        SqlFileTestModel model = new SqlFileTestModel();
        model.setConfigPath(configPath);
        model.setJdbcManagerName(jdbcManagerName);
        model.setPackageName(packageName);
        model.setShortClassName(shortClassName);
        model.setUseS2junit4(useS2junit4);
        for (String sqlFilePath : sqlFilePathList) {
            model.addSqlFilePath(sqlFilePath);
        }
        return model;
    }
}
