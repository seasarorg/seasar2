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
package org.seasar.extension.j2ee;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * @author higa
 *
 */
public class TransactionManagerWrapper implements TransactionManager {

	private TransactionManager tm_;

	public TransactionManagerWrapper() {
	}

	public TransactionManagerWrapper(TransactionManager tm) {
		setPhysicalTransactionManager(tm);
	}
	
	public TransactionManager getPhysicalTransactionManager() {
		return tm_;
	}
	
	protected void setPhysicalTransactionManager(TransactionManager tm) {
		tm_ = tm;
	}

	/**
	 * @see javax.transaction.TransactionManager#begin()
	 */
	public void begin() throws NotSupportedException, SystemException {
		tm_.begin();
	}

	/**
	 * @see javax.transaction.TransactionManager#commit()
	 */
	public void commit()
		throws
			RollbackException,
			HeuristicMixedException,
			HeuristicRollbackException,
			SecurityException,
			IllegalStateException,
			SystemException {
		
		tm_.commit();
	}

	/**
	 * @see javax.transaction.TransactionManager#getStatus()
	 */
	public int getStatus() throws SystemException {
		return tm_.getStatus();
	}

	/**
	 * @see javax.transaction.TransactionManager#getTransaction()
	 */
	public Transaction getTransaction() throws SystemException {
		return tm_.getTransaction();
	}

	/**
	 * @see javax.transaction.TransactionManager#resume(javax.transaction.Transaction)
	 */
	public void resume(Transaction tx)
		throws InvalidTransactionException, IllegalStateException, SystemException {
		
		tm_.resume(tx);
	}

	/**
	 * @see javax.transaction.TransactionManager#rollback()
	 */
	public void rollback()
		throws IllegalStateException, SecurityException, SystemException {

		tm_.rollback();
	}

	/**
	 * @see javax.transaction.TransactionManager#setRollbackOnly()
	 */
	public void setRollbackOnly()
		throws IllegalStateException, SystemException {

		tm_.setRollbackOnly();
	}

	/**
	 * @see javax.transaction.TransactionManager#setTransactionTimeout(int)
	 */
	public void setTransactionTimeout(int timeout) throws SystemException {
		tm_.setTransactionTimeout(timeout);
	}

	/**
	 * @see javax.transaction.TransactionManager#suspend()
	 */
	public Transaction suspend() throws SystemException {
		return tm_.suspend();
	}

}
