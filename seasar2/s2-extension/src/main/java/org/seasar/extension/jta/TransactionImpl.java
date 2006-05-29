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
package org.seasar.extension.jta;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.seasar.extension.jta.xa.XidImpl;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.exception.SRollbackException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.SLinkedList;

public final class TransactionImpl implements Transaction {

    private static final int VOTE_READONLY = 0;

    private static final int VOTE_COMMIT = 1;

    private static final int VOTE_ROLLBACK = 2;

    private static Logger logger_ = Logger.getLogger(TransactionImpl.class);

    private Xid xid_;

    private int status_ = Status.STATUS_NO_TRANSACTION;

    private List xaResourceWrappers_ = new ArrayList();

    private List synchronizations_ = new ArrayList();

    private boolean suspended_ = false;

    private int branchId_ = 0;

    public TransactionImpl() {
    }

    public void begin() {
        status_ = Status.STATUS_ACTIVE;
        init();
        if (logger_.isDebugEnabled()) {
            logger_.log("DSSR0003", null);
        }
    }

    public void suspend() throws XAException {
        assertNotSuspended();
        assertActive();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            xarw.end(XAResource.TMSUSPEND);
        }
        suspended_ = true;
    }

    private void assertNotSuspended() throws IllegalStateException {
        if (suspended_) {
            throw new SIllegalStateException("ESSR0314", null);
        }
    }

    private void assertActive() throws IllegalStateException {
        switch (status_) {
        case Status.STATUS_ACTIVE:
            break;
        default:
            throwIllegalStateException();
        }
    }

    private void throwIllegalStateException() throws IllegalStateException {
        switch (status_) {
        case Status.STATUS_PREPARING:
            throw new SIllegalStateException("ESSR0304", null);
        case Status.STATUS_PREPARED:
            throw new SIllegalStateException("ESSR0305", null);
        case Status.STATUS_COMMITTING:
            throw new SIllegalStateException("ESSR0306", null);
        case Status.STATUS_COMMITTED:
            throw new SIllegalStateException("ESSR0307", null);
        case Status.STATUS_MARKED_ROLLBACK:
            throw new SIllegalStateException("ESSR0308", null);
        case Status.STATUS_ROLLING_BACK:
            throw new SIllegalStateException("ESSR0309", null);
        case Status.STATUS_ROLLEDBACK:
            throw new SIllegalStateException("ESSR0310", null);
        case Status.STATUS_NO_TRANSACTION:
            throw new SIllegalStateException("ESSR0311", null);
        case Status.STATUS_UNKNOWN:
            throw new SIllegalStateException("ESSR0312", null);
        default:
            throw new SIllegalStateException("ESSR0032", new Object[] { String
                    .valueOf(status_) });
        }
    }

    private int getXAResourceWrapperSize() {
        return xaResourceWrappers_.size();
    }

    private XAResourceWrapper getXAResourceWrapper(int index) {
        return (XAResourceWrapper) xaResourceWrappers_.get(index);
    }

    public void resume() throws XAException {
        assertSuspended();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            xarw.start(XAResource.TMRESUME);
        }
        suspended_ = false;
    }

    private void assertSuspended() throws IllegalStateException {
        if (!suspended_) {
            throw new SIllegalStateException("ESSR0315", null);
        }
    }

    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {

        try {
            assertNotSuspended();
            assertActive();
            beforeCompletion();
            endResources(XAResource.TMSUCCESS);
            if (status_ == Status.STATUS_ACTIVE) {
                if (getXAResourceWrapperSize() == 0) {
                    status_ = Status.STATUS_COMMITTED;
                } else if (getXAResourceWrapperSize() == 1) {
                    commitOnePhase();
                } else {
                    switch (prepareResources()) {
                    case VOTE_READONLY:
                        status_ = Status.STATUS_COMMITTED;
                        break;
                    case VOTE_COMMIT:
                        commitTwoPhase();
                        break;
                    case VOTE_ROLLBACK:
                        rollbackForVoteOK();
                        afterCompletion();
                        throw new SRollbackException("ESSR0303",
                                new Object[] { toString() });
                    }
                }
            }
            if (logger_.isDebugEnabled()) {
                logger_.log("DSSR0004", null);
            }
            afterCompletion();
        } finally {
            destroy();
        }
    }

    private void beforeCompletion() {
        for (int i = 0; i < getSynchronizationSize(); ++i) {
            Synchronization sync = getSynchronization(i);
            try {
                sync.beforeCompletion();
            } catch (RuntimeException ex) {
                status_ = Status.STATUS_MARKED_ROLLBACK;
                endResources(XAResource.TMFAIL);
                rollbackResources();
                afterCompletion();
                throw ex;
            }
        }
    }

    private void endResources(int flag) {
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                xarw.end(flag);
            } catch (Throwable t) {
                logger_.log(t);
                status_ = Status.STATUS_MARKED_ROLLBACK;
            }
        }
    }

    private void commitOnePhase() {
        status_ = Status.STATUS_COMMITTING;
        XAResourceWrapper xari = getXAResourceWrapper(0);
        try {
            xari.commit(true);
            status_ = Status.STATUS_COMMITTED;
        } catch (XAException ex) {
            logger_.log(ex);
            status_ = Status.STATUS_UNKNOWN;
        }
    }

    private int prepareResources() {
        status_ = Status.STATUS_PREPARING;
        int vote = VOTE_READONLY;
        SLinkedList xarwList = new SLinkedList();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xarw.isCommitTarget()) {
                xarwList.addFirst(xarw);
            }
        }
        for (int i = 0; i < xarwList.size(); ++i) {
            XAResourceWrapper xarw = (XAResourceWrapper) xarwList.get(i);
            try {
                if (i == xarwList.size() - 1) {
                    // last resource commit optimization
                    xarw.commit(true);
                    xarw.setVoteOk(false);
                    vote = VOTE_COMMIT;
                } else if (xarw.prepare() == XAResource.XA_OK) {
                    vote = VOTE_COMMIT;
                } else {
                    xarw.setVoteOk(false);
                }
            } catch (XAException ex) {
                xarw.setVoteOk(false);
                status_ = Status.STATUS_MARKED_ROLLBACK;
                return VOTE_ROLLBACK;
            }
        }
        if (status_ == Status.STATUS_PREPARING) {
            status_ = Status.STATUS_PREPARED;
        }
        return vote;
    }

    private void commitTwoPhase() {
        status_ = Status.STATUS_COMMITTING;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xarw.isCommitTarget() && xarw.isVoteOk()) {
                try {
                    xarw.commit(false);
                } catch (Throwable t) {
                    logger_.log(t);
                    status_ = Status.STATUS_UNKNOWN;
                }
            }
        }
        if (status_ == Status.STATUS_COMMITTING) {
            status_ = Status.STATUS_COMMITTED;
        }
    }

    private void rollbackForVoteOK() {
        status_ = Status.STATUS_ROLLING_BACK;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xarw.isVoteOk()) {
                try {
                    xarw.rollback();
                } catch (Throwable t) {
                    logger_.log(t);
                    status_ = Status.STATUS_UNKNOWN;
                }
            }
        }
        if (status_ == Status.STATUS_ROLLING_BACK) {
            status_ = Status.STATUS_ROLLEDBACK;
        }
    }

    private void afterCompletion() {
        for (int i = 0; i < getSynchronizationSize(); ++i) {
            Synchronization sync = getSynchronization(i);
            try {
                sync.afterCompletion(status_);
            } catch (Throwable t) {
                logger_.log(t);
            }
        }
    }

    private int getSynchronizationSize() {
        return synchronizations_.size();
    }

    private Synchronization getSynchronization(int index) {
        return (Synchronization) synchronizations_.get(index);
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {

        try {
            assertNotSuspended();
            assertActiveOrMarkedRollback();
            endResources(XAResource.TMFAIL);
            rollbackResources();
            if (logger_.isDebugEnabled()) {
                logger_.log("DSSR0005", null);
            }
            afterCompletion();
        } finally {
            destroy();
        }
    }

    private void assertActiveOrMarkedRollback() throws IllegalStateException {
        switch (status_) {
        case Status.STATUS_ACTIVE:
        case Status.STATUS_MARKED_ROLLBACK:
            break;
        default:
            throwIllegalStateException();
        }
    }

    private void rollbackResources() {
        status_ = Status.STATUS_ROLLING_BACK;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                if (xarw.isCommitTarget()) {
                    xarw.rollback();
                }
            } catch (Throwable t) {
                logger_.log(t);
                status_ = Status.STATUS_UNKNOWN;
            }
        }
        if (status_ == Status.STATUS_ROLLING_BACK) {
            status_ = Status.STATUS_ROLLEDBACK;
        }
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {

        assertNotSuspended();
        assertActiveOrPreparingOrPrepared();
        status_ = Status.STATUS_MARKED_ROLLBACK;
    }

    private void assertActiveOrPreparingOrPrepared()
            throws IllegalStateException {
        switch (status_) {
        case Status.STATUS_ACTIVE:
        case Status.STATUS_PREPARING:
        case Status.STATUS_PREPARED:
            break;
        default:
            throwIllegalStateException();
        }
    }

    public boolean enlistResource(XAResource xaResource)
            throws RollbackException, IllegalStateException, SystemException {

        boolean oracled = xaResource.getClass().getName().startsWith("oracle");
        assertNotSuspended();
        assertActive();
        Xid xid = null;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xaResource.equals(xarw.getXAResource())) {
                return false;
            } else if (oracled) {
                continue;
            } else {
                try {
                    if (xaResource.isSameRM(xarw.getXAResource())) {
                        xid = xarw.getXid();
                        break;
                    }
                } catch (XAException ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        }
        int flag = xid == null ? XAResource.TMNOFLAGS : XAResource.TMJOIN;
        boolean commitTarget = xid == null ? true : false;
        if (xid == null) {
            xid = createXidBranch();
        }
        try {
            xaResource.start(xid, flag);
            xaResourceWrappers_.add(new XAResourceWrapper(xaResource, xid,
                    commitTarget));
            return true;
        } catch (XAException ex) {
            IllegalStateException ise = new IllegalStateException(ex.toString());
            ise.initCause(ex);
            throw ise;
        }
    }

    private Xid createXidBranch() {
        return new XidImpl(xid_, ++branchId_);
    }

    public boolean delistResource(XAResource xaResource, int flag)
            throws IllegalStateException, SystemException {

        assertNotSuspended();
        assertActiveOrMarkedRollback();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xaResource.equals(xarw.getXAResource())) {
                try {
                    xarw.end(flag);
                    return true;
                } catch (XAException ex) {
                    logger_.log(ex);
                    status_ = Status.STATUS_MARKED_ROLLBACK;
                    return false;
                }
            }
        }
        throw new SIllegalStateException("ESSR0313", null);
    }

    public int getStatus() {
        return status_;
    }

    public void registerSynchronization(Synchronization sync)
            throws RollbackException, IllegalStateException, SystemException {

        assertNotSuspended();
        assertActive();
        synchronizations_.add(sync);
    }

    public Xid getXid() {
        return xid_;
    }

    public boolean isSuspended() {
        return suspended_;
    }

    private void init() {
        xid_ = new XidImpl();
    }

    private void destroy() {
        status_ = Status.STATUS_NO_TRANSACTION;
        xaResourceWrappers_.clear();
        synchronizations_.clear();
        suspended_ = false;
    }
}
