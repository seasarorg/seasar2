package org.seasar.extension.dbcp.impl;

import java.sql.SQLException;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;

public class DummyConnectionPool implements ConnectionPool {

	private boolean checkIned_ = false;
	private boolean released_ = false;
	
	public DummyConnectionPool() {
	}

	public ConnectionWrapper checkOut() throws SQLException {
		return null;
	}
	public void checkIn(ConnectionWrapper connectionWrapper) {
		checkIned_ = true;
		connectionWrapper.cleanup();
	}
	
	public boolean isCheckIned() {
		return checkIned_;
	}

	public void release(ConnectionWrapper connectionWrapper) {
		released_ = true;
	}
	
	public boolean isReleased() {
		return released_;
	}

	public int getActivePoolSize() {
		return 0;
	}

	public int getMaxPoolSize() {
		return 0;
	}

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
