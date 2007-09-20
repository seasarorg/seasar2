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
