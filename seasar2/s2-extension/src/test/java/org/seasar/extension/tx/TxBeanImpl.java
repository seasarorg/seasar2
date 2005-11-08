package org.seasar.extension.tx;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public class TxBeanImpl implements TxBean {

	private TransactionManager tm_;

	public TxBeanImpl(TransactionManager tm) {
		tm_ = tm;
	}

	public boolean hasTransaction() throws SystemException {
		System.out.println(tm_.getTransaction());
		return tm_.getStatus() != Status.STATUS_NO_TRANSACTION;
	}

}
