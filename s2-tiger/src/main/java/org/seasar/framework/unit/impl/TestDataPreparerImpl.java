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
package org.seasar.framework.unit.impl;

import java.util.List;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.unit.DataAccessor;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.unit.TestDataPreparer;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * テストデータを準備するインターフェースの実装クラスです。
 * <p>
 * テストデータはExcelから読みデータベースへ書き込みます。
 * </p>
 * 
 * @author taedium
 */
public class TestDataPreparerImpl implements TestDataPreparer {

    /** ロガー */
    protected static final Logger logger = Logger
            .getLogger(TestDataPreparerImpl.class);

    /** テストデータを持つExcelのパスのリスト */
    protected final List<String> testDataXlsPaths = CollectionsUtil
            .newArrayList();

    /** データアクセッサー */
    protected DataAccessor dataAccessor;

    /** データベースのデータをテストデータで置換するかどうかを表すフラグ。デフォルトは<code>false</code> */
    protected boolean replaceDb;

    /**
     * データアクセッサーを設定します。
     * 
     * @param dataAccessor
     *            データアクセッサー
     */
    @Binding(bindingType = BindingType.MUST)
    public void setDataAccessor(final DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    /**
     * データベースのデータをテストデータで置換する場合<code>true</code>を設定します。
     * 
     * @param replaceDb
     *            データベースのデータをテストデータで置換する場合<code>true</code>、置換しないで追加する場合<code>false</code>
     */
    public void setReplaceDb(final boolean replaceDb) {
        this.replaceDb = replaceDb;
    }

    /**
     * テストデータを持つExcelのパスを登録します。
     * 
     * @param path
     *            テストデータを持つExcelのパス
     */
    public void addTestDataXlsPath(final String path) {
        testDataXlsPaths.add(path);
    }

    public void prepare(final TestContext testContext) {
        final String dirPath = testContext.getTestClassPackagePath();
        final boolean trimString = testContext.isTrimString();
        for (final String path : testDataXlsPaths) {
            if (ResourceUtil.isExist(path)) {
                readXlsWriteDb(path, trimString);
                return;
            }
            final String newPath = dirPath + "/" + path;
            if (ResourceUtil.isExist(newPath)) {
                readXlsWriteDb(newPath, trimString);
                return;
            }
        }
    }

    /**
     * Excelから読み込んだデータをデータベースに書き込みます。
     * 
     * @param path
     *            Excelのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     */
    protected void readXlsWriteDb(final String path, final boolean trimString) {
        if (replaceDb) {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0102", new Object[] { path });
            }
            dataAccessor.readXlsReplaceDb(path, trimString);
        } else {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0103", new Object[] { path });
            }
            dataAccessor.readXlsWriteDb(path, trimString);
        }
    }

}
