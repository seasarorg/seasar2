package org.seasar.extension.tx;

public interface ExceptionBean {

	public void invoke() throws Exception;

	public void invoke(Throwable t) throws Throwable;
}
