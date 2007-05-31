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

import org.seasar.extension.dataset.DataSet;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.unit.DataAccessor;
import org.seasar.framework.unit.ExpectedDataReader;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 期待値を読み込む実装クラスです。
 * <p>
 * 期待値はExcelから読み込みます。
 * </p>
 * 
 * @author taedium
 */
public class ExpectedDataReaderImpl implements ExpectedDataReader {

    /** ロガー */
    protected static final Logger logger = Logger
            .getLogger(ExpectedDataReaderImpl.class);

    /** 期待値が記述されたExcelのパスのリスト */
    protected final List<String> expectedDataXlsPaths = CollectionsUtil
            .newArrayList();

    /** データアクセッサー */
    protected DataAccessor dataAccessor;

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
     * 期待値が記述されたExcelのパスを登録します。
     * 
     * @param path
     *            期待値が記述されたExcelのパス
     */
    public void addExpectedDataXlsPath(final String path) {
        expectedDataXlsPaths.add(path);
    }

    public DataSet read(TestContext testContext) {
        final String dirPath = testContext.getTestClassPackagePath();
        final boolean trimString = testContext.isTrimString();
        for (final String path : expectedDataXlsPaths) {
            if (ResourceUtil.isExist(path)) {
                return readXls(path, trimString);
            }
            final String newPath = dirPath + "/" + path;
            if (ResourceUtil.isExist(newPath)) {
                return readXls(newPath, trimString);
            }
        }
        return null;
    }

    /**
     * 指定されたExcelを読みデータセットとして返します。
     * 
     * @param path
     *            Excelのパス
     * @param trimString
     *            文字列に含まれる空白を取り除く場合<code>true</code>
     * @return Excel内のデータのデータセット表現
     */
    protected DataSet readXls(final String path, final boolean trimString) {
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0104", new Object[] { path });
        }
        return dataAccessor.readXls(path, trimString);
    }
}
