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
package org.seasar.extension.jta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.seasar.extension.jta.xa.XidImpl;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.exception.SRollbackException;
import org.seasar.framework.exception.SSystemException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.SLinkedList;

/**
 * {@link javax.transaction.Transaction}の実装クラスです。
 * 
 * @author higa
 */
public class TransactionImpl implements ExtendedTransaction,
        SynchronizationRegister {

    private static final int VOTE_READONLY = 0;

    private static final int VOTE_COMMIT = 1;

    private static final int VOTE_ROLLBACK = 2;

    private static Logger logger = Logger.getLogger(TransactionImpl.class);

    private Xid xid;

    private int status = Status.STATUS_NO_TRANSACTION;

    private List xaResourceWrappers = new ArrayList();

    private List synchronizations = new ArrayList();

    private List interposedSynchronizations = new ArrayList();

    private Map resourceMap = new HashMap();

    private boolean suspended = false;

    private int branchId = 0;

    /**
     * <code>TransactionImpl</code>のインスタンスを構築します。
     * 
     */
    public TransactionImpl() {
    }

    /**
     * トランザクションを開始します。
     * 
     */
    public void begin() throws NotSupportedException, SystemException {
        status = Status.STATUS_ACTIVE;
        init();
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0003", new Object[] { this });
        }
    }

    /**
     * トランザクションを中断します。
     * 
     * @throws XAException
     *             <code>XAResource</code>を中断できなかった場合にスローされます
     */
    public void suspend() throws SystemException {
        assertNotSuspended();
        assertActiveOrMarkedRollback();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                xarw.end(XAResource.TMSUSPEND);
            } catch (final XAException ex) {
                throw new SSystemException("ESSR0363", new Object[] { ex }, ex);
            }
        }
        suspended = true;
    }

    private void assertNotSuspended() throws IllegalStateException {
        if (suspended) {
            throw new SIllegalStateException("ESSR0314", null);
        }
    }

    private void assertActive() throws IllegalStateException {
        switch (status) {
        case Status.STATUS_ACTIVE:
            break;
        default:
            throwIllegalStateException();
        }
    }

    private void throwIllegalStateException() throws IllegalStateException {
        switch (status) {
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
                    .valueOf(status) });
        }
    }

    private int getXAResourceWrapperSize() {
        return xaResourceWrappers.size();
    }

    private XAResourceWrapper getXAResourceWrapper(int index) {
        return (XAResourceWrapper) xaResourceWrappers.get(index);
    }

    /**
     * トランザクションを再開します。
     * 
     * @throws XAException
     *             <code>XAResource</code>を再開できなかった場合にスローされます
     */
    public void resume() throws SystemException {
        assertSuspended();
        assertActiveOrMarkedRollback();
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                xarw.start(XAResource.TMRESUME);
            } catch (final XAException ex) {
                throw new SSystemException("ESSR0364", new Object[] { ex }, ex);
            }
        }
        suspended = false;
    }

    private void assertSuspended() throws IllegalStateException {
        if (!suspended) {
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
            if (status == Status.STATUS_ACTIVE) {
                endResources(XAResource.TMSUCCESS);
                if (getXAResourceWrapperSize() == 0) {
                    status = Status.STATUS_COMMITTED;
                } else if (getXAResourceWrapperSize() == 1) {
                    commitOnePhase();
                } else {
                    switch (prepareResources()) {
                    case VOTE_READONLY:
                        status = Status.STATUS_COMMITTED;
                        break;
                    case VOTE_COMMIT:
                        commitTwoPhase();
                        break;
                    case VOTE_ROLLBACK:
                        rollbackForVoteOK();
                    }
                }
                if (status == Status.STATUS_COMMITTED) {
                    if (logger.isDebugEnabled()) {
                        logger.log("DSSR0004", new Object[] { this });
                    }
                }
            }
            final boolean rolledBack = status != Status.STATUS_COMMITTED;
            afterCompletion();
            if (rolledBack) {
                throw new SRollbackException("ESSR0303",
                        new Object[] { toString() });
            }
        } finally {
            destroy();
        }
    }

    private void beforeCompletion() {
        for (int i = 0; i < getSynchronizationSize()
                && status == Status.STATUS_ACTIVE; ++i) {
            beforeCompletion(getSynchronization(i));
        }
        for (int i = 0; i < getInterposedSynchronizationSize()
                && status == Status.STATUS_ACTIVE; ++i) {
            beforeCompletion(getInterposedSynchronization(i));
        }
    }

    private void beforeCompletion(Synchronization sync) {
        try {
            sync.beforeCompletion();
        } catch (Throwable t) {
            logger.log(t);
            status = Status.STATUS_MARKED_ROLLBACK;
            endResources(XAResource.TMFAIL);
            rollbackResources();
        }
    }

    private void endResources(int flag) {
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                xarw.end(flag);
            } catch (Throwable t) {
                logger.log(t);
                status = Status.STATUS_MARKED_ROLLBACK;
            }
        }
    }

    private void commitOnePhase() {
        status = Status.STATUS_COMMITTING;
        XAResourceWrapper xari = getXAResourceWrapper(0);
        try {
            xari.commit(true);
            status = Status.STATUS_COMMITTED;
        } catch (Throwable t) {
            logger.log(t);
            status = Status.STATUS_UNKNOWN;
        }
    }

    private int prepareResources() {
        status = Status.STATUS_PREPARING;
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
            } catch (Throwable t) {
                logger.log(t);
                xarw.setVoteOk(false);
                status = Status.STATUS_MARKED_ROLLBACK;
                return VOTE_ROLLBACK;
            }
        }
        if (status == Status.STATUS_PREPARING) {
            status = Status.STATUS_PREPARED;
        }
        return vote;
    }

    private void commitTwoPhase() {
        status = Status.STATUS_COMMITTING;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xarw.isCommitTarget() && xarw.isVoteOk()) {
                try {
                    xarw.commit(false);
                } catch (Throwable t) {
                    logger.log(t);
                    status = Status.STATUS_UNKNOWN;
                }
            }
        }
        if (status == Status.STATUS_COMMITTING) {
            status = Status.STATUS_COMMITTED;
        }
    }

    private void rollbackForVoteOK() {
        status = Status.STATUS_ROLLING_BACK;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            if (xarw.isVoteOk()) {
                try {
                    xarw.rollback();
                } catch (Throwable t) {
                    logger.log(t);
                    status = Status.STATUS_UNKNOWN;
                }
            }
        }
        if (status == Status.STATUS_ROLLING_BACK) {
            status = Status.STATUS_ROLLEDBACK;
        }
    }

    private void afterCompletion() {
        final int status = this.status;
        this.status = Status.STATUS_NO_TRANSACTION;
        for (int i = 0; i < getInterposedSynchronizationSize(); ++i) {
            afterCompletion(status, getInterposedSynchronization(i));
        }
        for (int i = 0; i < getSynchronizationSize(); ++i) {
            afterCompletion(status, getSynchronization(i));
        }
    }

    private void afterCompletion(final int status, final Synchronization sync) {
        try {
            sync.afterCompletion(status);
        } catch (Throwable t) {
            logger.log(t);
        }
    }

    private int getSynchronizationSize() {
        return synchronizations.size();
    }

    private Synchronization getSynchronization(int index) {
        return (Synchronization) synchronizations.get(index);
    }

    private int getInterposedSynchronizationSize() {
        return interposedSynchronizations.size();
    }

    private Synchronization getInterposedSynchronization(int index) {
        return (Synchronization) interposedSynchronizations.get(index);
    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {

        try {
            assertNotSuspended();
            assertActiveOrMarkedRollback();
            endResources(XAResource.TMFAIL);
            rollbackResources();
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0005", new Object[] { this });
            }
            afterCompletion();
        } finally {
            destroy();
        }
    }

    private void assertActiveOrMarkedRollback() throws IllegalStateException {
        switch (status) {
        case Status.STATUS_ACTIVE:
        case Status.STATUS_MARKED_ROLLBACK:
            break;
        default:
            throwIllegalStateException();
        }
    }

    private void rollbackResources() {
        status = Status.STATUS_ROLLING_BACK;
        for (int i = 0; i < getXAResourceWrapperSize(); ++i) {
            XAResourceWrapper xarw = getXAResourceWrapper(i);
            try {
                if (xarw.isCommitTarget()) {
                    xarw.rollback();
                }
            } catch (Throwable t) {
                logger.log(t);
                status = Status.STATUS_UNKNOWN;
            }
        }
        if (status == Status.STATUS_ROLLING_BACK) {
            status = Status.STATUS_ROLLEDBACK;
        }
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {

        assertNotSuspended();
        assertActiveOrPreparingOrPrepared();
        status = Status.STATUS_MARKED_ROLLBACK;
    }

    private void assertActiveOrPreparingOrPrepared()
            throws IllegalStateException {
        switch (status) {
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
            xaResourceWrappers.add(new XAResourceWrapper(xaResource, xid,
                    commitTarget));
            return true;
        } catch (XAException ex) {
            IllegalStateException ise = new IllegalStateException(ex.toString());
            ise.initCause(ex);
            throw ise;
        }
    }

    private Xid createXidBranch() {
        return new XidImpl(xid, ++branchId);
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
                    logger.log(ex);
                    status = Status.STATUS_MARKED_ROLLBACK;
                    return false;
                }
            }
        }
        throw new SIllegalStateException("ESSR0313", null);
    }

    public int getStatus() {
        return status;
    }

    public void registerSynchronization(Synchronization sync)
            throws RollbackException, IllegalStateException, SystemException {

        assertNotSuspended();
        assertActive();
        synchronizations.add(sync);
    }

    public void registerInterposedSynchronization(Synchronization sync)
            throws IllegalStateException {

        assertNotSuspended();
        assertActive();
        interposedSynchronizations.add(sync);
    }

    public void putResource(Object key, Object value)
            throws IllegalStateException {
        assertNotSuspended();
        resourceMap.put(key, value);
    }

    public Object getResource(Object key) throws IllegalStateException {
        assertNotSuspended();
        return resourceMap.get(key);
    }

    /**
     * トランザクションIDを返します。
     * 
     * @return トランザクションID
     */
    public Xid getXid() {
        return xid;
    }

    /**
     * トランザクションが中断されている場合は<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @return トランザクションが中断されている場合は<code>true</code>
     */
    public boolean isSuspended() {
        return suspended;
    }

    private void init() {
        xid = new XidImpl();
    }

    private void destroy() {
        status = Status.STATUS_NO_TRANSACTION;
        xaResourceWrappers.clear();
        synchronizations.clear();
        interposedSynchronizations.clear();
        resourceMap.clear();
        suspended = false;
    }

    public String toString() {
        return xid.toString();
    }

    /**
     * {@link Synchronization}のリストを返します。
     * 
     * @return
     */
    public List getSynchronizations() {
        return synchronizations;
    }

    /**
     * {@link Synchronization}のリストを返します。
     * 
     * @return
     */
    public List getInterposedSynchronizations() {
        return interposedSynchronizations;
    }
}
