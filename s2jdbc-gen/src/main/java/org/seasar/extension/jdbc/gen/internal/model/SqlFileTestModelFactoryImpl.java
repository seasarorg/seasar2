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
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.JdbcManager;
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

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
    protected boolean useS2junit4;

    /** SQLファイルのパスのリスト */
    protected List<String> sqlFilePathList;

    /** SQLファイルのサポート */
    protected SqlFileSupport sqlFileSupport;

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

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
        this(classpathDir, sqlFileSet, configPath, jdbcManagerName,
                packageName, shortClassName, useS2junit4, new SqlFileSupport());
    }

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
     * @param sqlFileSupport
     *            SQLファイルのサポート
     */
    protected SqlFileTestModelFactoryImpl(File classpathDir,
            Set<File> sqlFileSet, String configPath, String jdbcManagerName,
            String packageName, String shortClassName, boolean useS2junit4,
            SqlFileSupport sqlFileSupport) {
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
        if (sqlFileSupport == null) {
            throw new NullPointerException("sqlFileSupport");
        }
        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.packageName = packageName;
        this.shortClassName = shortClassName;
        this.useS2junit4 = useS2junit4;
        this.sqlFileSupport = sqlFileSupport;
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
        return sqlFileSupport.createSqlFilePathList(classpathDir, sqlFileSet);
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
        doGeneratedInfo(model);
        return model;
    }

    /**
     * 生成情報を処理します。
     * 
     * @param sqlFileTestModel
     *            SQLファイルテストモデル
     */
    protected void doGeneratedInfo(SqlFileTestModel sqlFileTestModel) {
        generatedModelSupport.fillGeneratedInfo(this, sqlFileTestModel);
    }
}
