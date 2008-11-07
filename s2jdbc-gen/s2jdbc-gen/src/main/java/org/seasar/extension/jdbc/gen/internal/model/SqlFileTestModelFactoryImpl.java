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
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModelFactory;

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

    /** SQLファイルのパスのリスト */
    protected List<String> sqlFilePathList = new ArrayList<String>();

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            ルートディレクトリ
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
     */
    public SqlFileTestModelFactoryImpl(File classpathDir, Set<File> sqlFileSet,
            String configPath, String jdbcManagerName, String packageName,
            String shortClassName) {
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

        String basePath = FileUtil.getCanonicalPath(classpathDir)
                + File.separator;
        for (File sqlFile : sqlFileSet) {
            String path = FileUtil.getCanonicalPath(sqlFile);
            if (path.startsWith(basePath)) {
                path = path.substring(basePath.length());
                sqlFilePathList.add(path.replace(File.separator, "/"));
            }
        }
        Collections.sort(sqlFilePathList);
    }

    public SqlFileTestModel getSqlFileTestModel() {
        SqlFileTestModel model = new SqlFileTestModel();
        model.setConfigPath(configPath);
        model.setJdbcManagerName(jdbcManagerName);
        model.setPackageName(packageName);
        model.setShortClassName(shortClassName);
        for (String sqlFilePath : sqlFilePathList) {
            model.addSqlFilePath(sqlFilePath);
        }
        return model;
    }

}
