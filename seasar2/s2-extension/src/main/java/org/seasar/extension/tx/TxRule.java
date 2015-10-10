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
package org.seasar.extension.tx;

import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * 例外が発生した場合にトランザクションをコミットするかロールバックするかのルールを表現します。
 * 
 * @author koichik
 */
public class TxRule {

    /** 例外クラス */
    protected final Class exceptionClass;

    /** 例外が発生した場合にコミットする場合は<code>true</code> */
    protected final boolean commit;

    /**
     * インスタンスを構築します。
     * 
     * @param exceptionClass
     *            例外クラス
     * @param commit
     *            コミットする場合は<code>true</code>、ロールバックする場合は<code>false</code>
     */
    public TxRule(final Class exceptionClass, final boolean commit) {
        if (!Throwable.class.isAssignableFrom(exceptionClass)) {
            throw new SIllegalArgumentException("ESSR0365",
                    new Object[] { exceptionClass.getName() });
        }
        this.exceptionClass = exceptionClass;
        this.commit = commit;
    }

    /**
     * 例外がこのルールに適合する場合は<code>true</code>、それ以外の場合は<code>false</code>を返します。
     * 
     * @param t
     *            例外
     * @return 例外がこのルールに適合する場合は<code>true</code>
     */
    public boolean isAssignableFrom(final Throwable t) {
        return exceptionClass.isAssignableFrom(t.getClass());
    }

    /**
     * ルールに従ってトランザクションをロールバックするようマークします。
     * 
     * @param adapter
     *            トランザクションマネージャへのアダプタ
     * @throws Exception
     *             トランザクション制御で例外が発生した場合にスローされます
     */
    public void complete(final TransactionManagerAdapter adapter) {
        if (!commit) {
            adapter.setRollbackOnly();
        }
    }

}
