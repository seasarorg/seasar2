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
package org.seasar.extension.dbcp.impl;

import java.sql.SQLException;

import javax.transaction.Transaction;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;

/**
 * @author higa
 * 
 */
public class DummyConnectionPool implements ConnectionPool {

    private boolean checkIned_ = false;

    private boolean released_ = false;

    /**
     * 
     */
    public DummyConnectionPool() {
    }

    public ConnectionWrapper checkOut() throws SQLException {
        return null;
    }

    public void checkIn(ConnectionWrapper connectionWrapper) {
        checkIned_ = true;
        connectionWrapper.cleanup();
    }

    public void checkInTx(Transaction tx) {
        checkIned_ = true;
    }

    /**
     * @return
     */
    public boolean isCheckIned() {
        return checkIned_;
    }

    public void release(ConnectionWrapper connectionWrapper) {
        released_ = true;
    }

    /**
     * @return
     */
    public boolean isReleased() {
        return released_;
    }

    public int getActivePoolSize() {
        return 0;
    }

    public int getMaxPoolSize() {
        return 0;
    }

    /**
     * @return
     */
    public int getMinPoolSize() {
        return 0;
    }

    public int getFreePoolSize() {
        return 0;
    }

    public void close() {
    }

    public int getTxActivePoolSize() {
        return 0;
    }

}
