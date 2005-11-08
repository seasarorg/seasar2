package org.seasar.extension.tx;

import javax.transaction.SystemException;

public interface TxBean {

	public boolean hasTransaction() throws SystemException;
}
