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
package org.seasar.extension.jta.xa;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.seasar.framework.exception.SXAException;

/**
 * {@link XAResource}の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractXAResource implements XAResource,
        XAResourceStatus {

    private Xid currentXid;

    private int status = RS_NONE;

    private int timeout = 0;

    /**
     * {@link AbstractXAResource}を作成します。
     */
    public AbstractXAResource() {
    }

    public void start(Xid xid, int flags) throws XAException {
        switch (flags) {
        case TMNOFLAGS:
            begin(xid);
            break;
        case TMRESUME:
            resume(xid);
            break;
        default:
            throw new SXAException("ESSR0032", new Object[] { String
                    .valueOf(flags) });
        }
    }

    private void begin(Xid xid) throws XAException {
        assertCurrentXidNull();
        doBegin(xid);
        currentXid = xid;
        status = RS_ACTIVE;
    }

    private void assertCurrentXidNull() throws XAException {
        if (currentXid != null) {
            throw new SXAException("ESSR0316", null);
        }
    }

    /**
     * トランザクションを開始します。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doBegin(Xid xid) throws XAException;

    private void resume(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusSuspended();
        doResume(xid);
        status = RS_ACTIVE;
    }

    private void assertCurrentXidSame(final Xid xid) throws XAException {
        if (currentXid != xid) {
            throw new SXAException("ESSR0319", new Object[] { xid, currentXid });
        }
    }

    private void assertStatusSuspended() throws XAException {
        if (status != RS_SUSPENDED) {
            throw new SXAException("ESSR0323", null);
        }
    }

    /**
     * レジュームします。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doResume(Xid xid) throws XAException;

    public void end(Xid xid, int flags) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusActive();
        switch (flags) {
        case TMSUSPEND:
            suspend(xid);
            break;
        case TMFAIL:
            fail(xid);
            break;
        case TMSUCCESS:
            success(xid);
            break;
        default:
            throw new SXAException("ESSR0032", new Object[] { String
                    .valueOf(flags) });
        }
    }

    private void assertStatusActive() throws XAException {
        if (status != RS_ACTIVE) {
            throw new SXAException("ESSR0324", null);
        }
    }

    private void suspend(Xid xid) throws XAException {
        doSuspend(xid);
        status = RS_SUSPENDED;
    }

    /**
     * サスペンドします。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doSuspend(Xid xid) throws XAException;

    private void fail(Xid xid) throws XAException {
        doFail(xid);
        status = RS_FAIL;
    }

    /**
     * 失敗させます。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     */
    protected abstract void doFail(Xid xid) throws XAException;

    private void success(Xid xid) throws XAException {
        doSuccess(xid);
        status = RS_SUCCESS;
    }

    /**
     * 成功時の処理を行ないます。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doSuccess(Xid xid) throws XAException;

    public int prepare(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusSuccess();
        int ret = doPrepare(xid);
        if (ret == XA_OK) {
            status = RS_PREPARED;
        } else {
            status = RS_NONE;
        }
        return ret;
    }

    private void assertStatusSuccess() throws XAException {
        if (status != RS_SUCCESS) {
            throw new SXAException("ESSR0320", null);
        }
    }

    /**
     * コミットする準備を行ないます。
     * 
     * @param xid
     *            トランザクション識別子
     * @return 投票の結果
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract int doPrepare(Xid xid) throws XAException;

    public void commit(Xid xid, boolean onePhase) throws XAException {
        assertCurrentXidSame(xid);
        if (onePhase) {
            assertStatusSuccess();
        } else {
            assertStatusPrepared();
        }
        doCommit(xid, onePhase);
        init();
    }

    private void assertStatusPrepared() throws XAException {
        if (status != RS_PREPARED) {
            throw new SXAException("ESSR0321", null);
        }
    }

    /**
     * コミットします。
     * 
     * @param xid
     *            トランザクション識別子
     * @param onePhase
     *            1フェーズかどうか
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doCommit(Xid xid, boolean onePhase)
            throws XAException;

    private void init() {
        currentXid = null;
        status = RS_NONE;
    }

    public void forget(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        doForget(xid);
        init();
    }

    /**
     * トランザクションを忘れます。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doForget(Xid xid) throws XAException;

    public Xid[] recover(final int flag) throws XAException {
        return null;
    }

    public void rollback(final Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusSuccessOrFailOrPrepared();
        doRollback(xid);
        init();
    }

    private void assertStatusSuccessOrFailOrPrepared() throws XAException {
        switch (status) {
        case RS_SUCCESS:
        case RS_FAIL:
        case RS_PREPARED:
            break;
        default:
            throw new SXAException("ESSR0322", null);
        }
    }

    /**
     * ロールバックします。
     * 
     * @param xid
     *            トランザクション識別子
     * @throws XAException
     *             XA例外が発生した場合
     */
    protected abstract void doRollback(Xid xid) throws XAException;

    public boolean isSameRM(XAResource xar) throws XAException {
        return false;
    }

    public int getTransactionTimeout() throws XAException {
        return timeout;
    }

    public boolean setTransactionTimeout(int timeout) throws XAException {
        this.timeout = timeout;
        return true;
    }

    /**
     * 現在のトランザクション識別子を返します。
     * 
     * @return 現在のトランザクション識別子
     */
    public Xid getCurrentXid() {
        return currentXid;
    }

    /**
     * ステータスを返します。
     * 
     * @return ステータス
     */
    public int getStatus() {
        return status;
    }
}