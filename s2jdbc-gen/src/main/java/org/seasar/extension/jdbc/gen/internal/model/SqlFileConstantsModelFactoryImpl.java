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

import javax.annotation.Generated;

import org.seasar.extension.jdbc.gen.model.SqlFileConstantFieldModel;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantNamingRule;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModel;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModelFactory;

/**
 * {@link SqlFileConstantsModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlFileConstantsModelFactoryImpl implements
        SqlFileConstantsModelFactory {

    /** SQLファイルのパスを表す定数の名前付けルール */
    protected SqlFileConstantNamingRule sqlFileConstantNamingRule;

    /** パッケージ名、パッケージ名を指定しない場合は{@code null} */
    protected String packageName;

    /** テストクラスの単純名 */
    protected String shortClassName;

    /** SQLファイルのパスのリスト */
    protected List<String> sqlFilePathList;

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /** クラスモデルサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** SQLファイルのサポート */
    protected SqlFileSupport sqlFileSupport = new SqlFileSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @param sqlFileConstantNamingRule
     *            SQLファイルのパスを表す定数の名前付けルール
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            テストクラスの単純名
     */
    public SqlFileConstantsModelFactoryImpl(File classpathDir,
            Set<File> sqlFileSet,
            SqlFileConstantNamingRule sqlFileConstantNamingRule,
            String packageName, String shortClassName) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (sqlFileSet == null) {
            throw new NullPointerException("sqlFileSet");
        }
        if (sqlFileConstantNamingRule == null) {
            throw new NullPointerException("sqlFileConstantNamingRule");
        }
        if (shortClassName == null) {
            throw new NullPointerException("shortClassName");
        }
        this.sqlFileConstantNamingRule = sqlFileConstantNamingRule;
        this.packageName = packageName;
        this.shortClassName = shortClassName;
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

    public SqlFileConstantsModel getSqlFileConstantsModel() {
        SqlFileConstantsModel model = new SqlFileConstantsModel();
        model.setPackageName(packageName);
        model.setShortClassName(shortClassName);
        for (String path : sqlFilePathList) {
            SqlFileConstantFieldModel fieldModel = createSqlFileConstantFieldModel(path);
            model.addSqlFileConstantFieldModel(fieldModel);
        }
        doGeneratedInfo(model);
        doImportName(model);
        return model;
    }

    /**
     * SQLファイル定数フィールドモデルを作成します。
     * 
     * @param path
     *            SQLファイルのパス
     * @return SQLファイル定数フィールドモデル
     */
    protected SqlFileConstantFieldModel createSqlFileConstantFieldModel(
            String path) {
        SqlFileConstantFieldModel fieldModel = new SqlFileConstantFieldModel();
        fieldModel.setName(sqlFileConstantNamingRule
                .fromPathToConstantName(path));
        fieldModel.setPath(path);
        return fieldModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param sqlFileConstantsModel
     *            SQLファイル定数モデル
     */
    protected void doImportName(SqlFileConstantsModel sqlFileConstantsModel) {
        classModelSupport.addImportName(sqlFileConstantsModel, Generated.class);
    }

    /**
     * 生成情報を処理します。
     * 
     * @param sqlFileConstantsModel
     *            SQLファイル定数モデル
     */
    protected void doGeneratedInfo(SqlFileConstantsModel sqlFileConstantsModel) {
        generatedModelSupport.fillGeneratedInfo(this, sqlFileConstantsModel);
    }
}
