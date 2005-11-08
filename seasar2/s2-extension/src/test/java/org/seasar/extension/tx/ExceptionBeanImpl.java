package org.seasar.extension.tx;

public class ExceptionBeanImpl implements ExceptionBean {

	public void invoke() throws Exception {
		throw new Exception("hoge");
	}

	public void invoke(Throwable t) throws Throwable {
	    throw t;
	}

}
