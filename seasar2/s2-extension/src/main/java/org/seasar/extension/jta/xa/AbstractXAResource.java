/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

public abstract class AbstractXAResource implements XAResource,
        XAResourceStatus {

    private Xid currentXid_;

    private int status_ = RS_NONE;

    private int timeout_ = 0;

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
        currentXid_ = xid;
        status_ = RS_ACTIVE;
    }

    private void assertCurrentXidNull() throws XAException {
        if (currentXid_ != null) {
            throw new SXAException("ESSR0316", null);
        }
    }

    protected abstract void doBegin(Xid xid) throws XAException;

    private void resume(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusSuspended();
        doResume(xid);
        status_ = RS_ACTIVE;
    }

    private void assertCurrentXidSame(final Xid xid) throws XAException {
        if (currentXid_ != xid) {
            throw new SXAException("ESSR0319",
                    new Object[] { xid, currentXid_ });
        }
    }

    private void assertStatusSuspended() throws XAException {
        if (status_ != RS_SUSPENDED) {
            throw new SXAException("ESSR0323", null);
        }
    }

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
        if (status_ != RS_ACTIVE) {
            throw new SXAException("ESSR0324", null);
        }
    }

    private void suspend(Xid xid) throws XAException {
        doSuspend(xid);
        status_ = RS_SUSPENDED;
    }

    protected abstract void doSuspend(Xid xid) throws XAException;

    private void fail(Xid xid) throws XAException {
        doFail(xid);
        status_ = RS_FAIL;
    }

    protected abstract void doFail(Xid xid) throws XAException;

    private void success(Xid xid) throws XAException {
        doSuccess(xid);
        status_ = RS_SUCCESS;
    }

    protected abstract void doSuccess(Xid xid) throws XAException;

    public int prepare(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        assertStatusSuccess();
        int ret = doPrepare(xid);
        if (ret == XA_OK) {
            status_ = RS_PREPARED;
        } else {
            status_ = RS_NONE;
        }
        return ret;
    }

    private void assertStatusSuccess() throws XAException {
        if (status_ != RS_SUCCESS) {
            throw new SXAException("ESSR0320", null);
        }
    }

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
        if (status_ != RS_PREPARED) {
            throw new SXAException("ESSR0321", null);
        }
    }

    protected abstract void doCommit(Xid xid, boolean onePhase)
            throws XAException;

    private void init() {
        currentXid_ = null;
        status_ = RS_NONE;
    }

    public void forget(Xid xid) throws XAException {
        assertCurrentXidSame(xid);
        doForget(xid);
        init();
    }

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
        switch (status_) {
        case RS_SUCCESS:
        case RS_FAIL:
        case RS_PREPARED:
            break;
        default:
            throw new SXAException("ESSR0322", null);
        }
    }

    protected abstract void doRollback(Xid xid) throws XAException;

    public boolean isSameRM(XAResource xar) throws XAException {
        return false;
    }

    public int getTransactionTimeout() throws XAException {
        return timeout_;
    }

    public boolean setTransactionTimeout(int timeout) throws XAException {
        timeout_ = timeout;
        return true;
    }

    public Xid getCurrentXid() {
        return currentXid_;
    }

    public int getStatus() {
        return status_;
    }
}