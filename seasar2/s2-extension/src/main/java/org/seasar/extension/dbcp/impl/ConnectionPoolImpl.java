/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.sql.XADataSource;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.timer.TimeoutManager;
import org.seasar.extension.timer.TimeoutTarget;
import org.seasar.extension.timer.TimeoutTask;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.SLinkedList;
import org.seasar.framework.util.TransactionManagerUtil;
import org.seasar.framework.util.TransactionUtil;

public final class ConnectionPoolImpl
	implements ConnectionPool, Synchronization {

	private static Logger logger_ = Logger.getLogger(ConnectionPoolImpl.class);
	private XADataSource xaDataSource_;
	private TransactionManager transactionManager_;
	private int timeout_ = 600;
	private int maxPoolSize_ = 10;
	private boolean allowLocalTx_ = true;
	private Set activePool_ = new HashSet();
	private Map txActivePool_ = new HashMap();
	private SLinkedList freePool_ = new SLinkedList();

	public ConnectionPoolImpl() {
	}

	public XADataSource getXADataSource() {
		return xaDataSource_;
	}

	public void setXADataSource(XADataSource xaDataSource) {
		xaDataSource_ = xaDataSource;
	}

	public TransactionManager getTransactionManager() {
		return transactionManager_;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		transactionManager_ = transactionManager;
	}

	public int getTimeout() {
		return timeout_;
	}

	public void setTimeout(int timeout) {
		timeout_ = timeout;
	}

	public int getMaxPoolSize() {
		return maxPoolSize_;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		maxPoolSize_ = maxPoolSize;
	}

	public boolean isAllowLocalTx() {
		return allowLocalTx_;
	}

	public void setAllowLocalTx(boolean allowLocalTx) {
		allowLocalTx_ = allowLocalTx;
	}

	public int getActivePoolSize() {
		return activePool_.size();
	}

	public int getTxActivePoolSize() {
		return txActivePool_.size();
	}

	public int getFreePoolSize() {
		return freePool_.size();
	}

	public synchronized ConnectionWrapper checkOut() throws SQLException {
		Transaction tx = getTransaction();
		if (tx == null && !isAllowLocalTx()) {
			throw new SIllegalStateException("ESSR0311", null);
		}

		ConnectionWrapper con = getConnectionTxActivePool(tx);
		if (con != null) {
			return con;
		}
		while (getMaxPoolSize() > 0 && getActivePoolSize() + getTxActivePoolSize()
			>= getMaxPoolSize()) {
			try {
				wait();
			} catch (InterruptedException ignore) {
			}
		}
		con = checkOutFreePool();
		if (con == null) {
			con = createConnection(tx == null);
		} else {
			con.init(tx == null);
		}
		if (tx != null) {
			TransactionUtil.enlistResource(tx, con.getXAResource());
			TransactionUtil.registerSynchronization(tx, this);
			setConnectionTxActivePool(tx, con);
		} else {
			setConnectionActivePool(con);
		}
		return con;
	}

	private Transaction getTransaction() {
		return TransactionManagerUtil.getTransaction(transactionManager_);
	}

	private ConnectionWrapper getConnectionTxActivePool(Transaction tx) {
		return (ConnectionWrapper) txActivePool_.get(tx);
	}

	private ConnectionWrapper checkOutFreePool() {
		if (freePool_.isEmpty()) {
			return null;
		}
		FreeItem item = (FreeItem) freePool_.removeLast();
		ConnectionWrapper con = item.getConnection();
		item.destroy(); 
		return con;
	}

	private ConnectionWrapper createConnection(boolean localTx) throws SQLException {
		ConnectionWrapper con =
			new ConnectionWrapperImpl(xaDataSource_.getXAConnection(), this, localTx);
		logger_.log("DSSR0006", null);
		return con;
	}

	private void setConnectionTxActivePool(
		Transaction tx,
		ConnectionWrapper connection) {

		txActivePool_.put(tx, connection);
	}

	private void setConnectionActivePool(ConnectionWrapper connection) {
		activePool_.add(connection);
	}

	public synchronized void release(ConnectionWrapper connection) {
		activePool_.remove(connection);
		Transaction tx = getTransaction();
		if (tx != null) {
			txActivePool_.remove(tx);
		}
		connection.closeReally();
		notify();
	}

	public synchronized void checkIn(ConnectionWrapper connection) {
		activePool_.remove(connection);
		connection.cleanup();
		if (getMaxPoolSize() > 0) {
			checkInFreePool(connection);
			notify();
		}
		else {
			connection.closeReally();
		}
	}

	private void checkInFreePool(ConnectionWrapper connection) {
		freePool_.addLast(new FreeItem(connection));
	}

	public void afterCompletion(int status) {
		switch (status) {
			case Status.STATUS_COMMITTED :
			case Status.STATUS_ROLLEDBACK :
				checkInTx();
				break;
		}
	}

	private synchronized void checkInTx() {
		Transaction tx = getTransaction();
		if (tx == null) {
			return;
		}
		ConnectionWrapper con = checkOutTxPool(tx);
		if (con == null) {
			return;
		}
		checkInFreePool(con);
		notify();
	}

	private ConnectionWrapper checkOutTxPool(Transaction tx) {
		return (ConnectionWrapper) txActivePool_.remove(tx);
	}

	public final void beforeCompletion() {
	}

	public final synchronized void close() {
		for (SLinkedList.Entry e = freePool_.getFirstEntry();
			e != null;
			e = e.getNext()) {
			FreeItem item = (FreeItem) e.getElement();
			item.getConnection().closeReally();
		}
		freePool_.clear();
		for (Iterator i = txActivePool_.values().iterator(); i.hasNext();) {
			ConnectionWrapper con = (ConnectionWrapper) i.next();
			con.closeReally();
		}
		txActivePool_.clear();
		for (Iterator i = activePool_.iterator(); i.hasNext();) {
			ConnectionWrapper con = (ConnectionWrapper) i.next();
			con.closeReally();
		}
		activePool_.clear();
	}

	private class FreeItem implements TimeoutTarget {

		private ConnectionWrapper connectionWrapper_;
		private TimeoutTask timeoutTask_;

		FreeItem(ConnectionWrapper connectionWrapper) {
			connectionWrapper_ = connectionWrapper;
			timeoutTask_ =
				TimeoutManager.getInstance().addTimeoutTarget(
					this,
					timeout_,
					false);
		}

		public ConnectionWrapper getConnection() {
			return connectionWrapper_;
		}

		public void expired() {
			synchronized (ConnectionPoolImpl.this) {
				freePool_.remove(this);
			}
			synchronized (this) {
				if (timeoutTask_ != null) {
					timeoutTask_.cancel();
					timeoutTask_ = null;
				}
				if (connectionWrapper_ != null) {
					connectionWrapper_.closeReally();
					connectionWrapper_ = null;
				}
			}
		}
		
		public synchronized void destroy() {
			if (timeoutTask_ != null) {
				timeoutTask_.cancel();
				timeoutTask_ = null;
			}
			connectionWrapper_ = null;
		}
	}
}
