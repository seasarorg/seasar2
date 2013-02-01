/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.tx.adapter;

import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.unit.EasyMockTestCase;

/**
 * @author koichik
 */
public abstract class AbstTransactionManagerAdapterTest extends
        EasyMockTestCase {

    /**
     * @author koichik
     */
    public static class ReturnCommitCallback implements TransactionCallback {

        public Object execute(TransactionManagerAdapter adapter)
                throws Throwable {
            return "hoge";
        }

    }

    /**
     * @author koichik
     */
    public static class ReturnRollbackCallback implements TransactionCallback {

        public Object execute(TransactionManagerAdapter adapter)
                throws Throwable {
            adapter.setRollbackOnly();
            return "hoge";
        }

    }

    /**
     * @author koichik
     */
    public static class ExceptionCommitCallback implements TransactionCallback {

        public Object execute(TransactionManagerAdapter adapter)
                throws Throwable {
            throw new Exception();
        }

    }

    /**
     * @author koichik
     */
    public static class ExceptionRollbackCallback implements
            TransactionCallback {

        public Object execute(TransactionManagerAdapter adapter)
                throws Throwable {
            adapter.setRollbackOnly();
            throw new Exception();
        }

    }

}
