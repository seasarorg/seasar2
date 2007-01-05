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
 * @author taedium
 * 
 */
public class TestDataPreparerImpl implements TestDataPreparer {

    protected static final Logger logger = Logger
            .getLogger(TestDataPreparerImpl.class);

    protected final List<String> testDataXlsPaths = CollectionsUtil
            .newArrayList();

    protected DataAccessor dataAccessor;

    protected boolean replaceDb;

    @Binding(bindingType = BindingType.MUST)
    public void setDataAccessor(final DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public void setReplaceDb(final boolean replaceDb) {
        this.replaceDb = replaceDb;
    }

    public void addTestDataXlsPath(final String path) {
        testDataXlsPaths.add(path);
    }

    public void prepare(final TestContext testContext) {
        final String dirPath = testContext.getTestClassPackagePath();
        for (final String path : testDataXlsPaths) {
            if (ResourceUtil.isExist(path)) {
                readXlsWriteDb(path);
                return;
            }
            final String newPath = dirPath + "/" + path;
            if (ResourceUtil.isExist(newPath)) {
                readXlsWriteDb(newPath);
                return;
            }
        }
    }

    protected void readXlsWriteDb(final String path) {
        if (replaceDb) {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0102", new Object[] { path });
            }
            dataAccessor.readXlsReplaceDb(path);
        } else {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0103", new Object[] { path });
            }
            dataAccessor.readXlsWriteDb(path);
        }
    }

}
