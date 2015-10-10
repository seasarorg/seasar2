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

import java.io.Serializable;

import javax.transaction.xa.Xid;

/**
 * {@link Xid}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class XidImpl implements Xid, Serializable {

    static final long serialVersionUID = 1L;

    private static final int FORMAT_ID = 0x1108;

    private static final byte[] INITIAL_BRANCH_ID = convert64bytes(new byte[0]);

    private static final String GLOBAL_ID_BASE = System.currentTimeMillis()
            + "/";

    private static int nextId = 0;

    private int hashCode;

    private byte[] globalId;

    private byte[] branchId;

    /**
     * {@link XidImpl}を作成します。
     */
    public XidImpl() {
        hashCode = getNextId();
        globalId = createGlobalId();
        branchId = INITIAL_BRANCH_ID;
    }

    /**
     * {@link XidImpl}を作成します。
     * 
     * @param xid
     *            トランザクション識別子
     * @param bid
     *            ブランチ識別子
     */
    public XidImpl(Xid xid, int bid) {
        hashCode = xid.hashCode();
        globalId = xid.getGlobalTransactionId();
        branchId = convert64bytes(Integer.toString(bid).getBytes());
    }

    private byte[] createGlobalId() {
        return convert64bytes((GLOBAL_ID_BASE + Integer.toString(hashCode))
                .getBytes());
    }

    public byte[] getGlobalTransactionId() {
        return (byte[]) globalId.clone();
    }

    public byte[] getBranchQualifier() {
        return (byte[]) branchId.clone();
    }

    public int getFormatId() {
        return FORMAT_ID;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof XidImpl)) {
            return false;
        }
        XidImpl other = (XidImpl) obj;
        if (hashCode != other.hashCode) {
            return false;
        }
        if (globalId.length != other.globalId.length
                || branchId.length != other.branchId.length) {

            return false;
        }
        for (int i = 0; i < globalId.length; ++i) {
            if (globalId[i] != other.globalId[i]) {
                return false;
            }
        }
        for (int i = 0; i < branchId.length; ++i) {
            if (branchId[i] != other.branchId[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return hashCode;
    }

    public String toString() {
        return "[FormatId=" + FORMAT_ID + ", GlobalId="
                + new String(globalId).trim() + ", BranchId="
                + new String(branchId).trim() + "]";
    }

    private static byte[] convert64bytes(byte[] bytes) {
        byte[] new64bytes = new byte[64];
        System.arraycopy(bytes, 0, new64bytes, 0, bytes.length);
        return new64bytes;
    }

    private static synchronized int getNextId() {
        return nextId++;
    }
}