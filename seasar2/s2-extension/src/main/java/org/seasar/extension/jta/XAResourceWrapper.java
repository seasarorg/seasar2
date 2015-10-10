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

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

class XAResourceWrapper {

    private XAResource xaResource_;

    private Xid xid_;

    private boolean commitTarget_;

    private boolean voteOk_ = true;

    XAResourceWrapper(XAResource xaResource, Xid xid, boolean commitTarget) {

        xaResource_ = xaResource;
        xid_ = xid;
        commitTarget_ = commitTarget;
    }

    XAResource getXAResource() {
        return xaResource_;
    }

    Xid getXid() {
        return xid_;
    }

    boolean isCommitTarget() {
        return commitTarget_;
    }

    boolean isVoteOk() {
        return voteOk_;
    }

    void setVoteOk(boolean voteOk) {
        voteOk_ = voteOk;
    }

    void start(int flag) throws XAException {
        xaResource_.start(xid_, flag);
    }

    void end(int flag) throws XAException {
        xaResource_.end(xid_, flag);
    }

    int prepare() throws XAException {
        return xaResource_.prepare(xid_);
    }

    void commit(boolean onePhase) throws XAException {
        xaResource_.commit(xid_, onePhase);
    }

    void rollback() throws XAException {
        xaResource_.rollback(xid_);
    }

    void forget() throws XAException {
        xaResource_.forget(xid_);
    }
}