package org.seasar.framework.ejb.unit;

import java.lang.reflect.Method;
import java.util.List;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.ejb.unit.annotation.Rollback;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 *
 */
public abstract class S2EJB3TestCase extends S2TestCase {
	
	public S2EJB3TestCase() {
	}

	public S2EJB3TestCase(String name) {
		super(name);
	}

	@Override
	protected boolean needTransaction() {
		Method m = ClassUtil.getMethod(getClass(), getName(), new Class[] {});
		return m.isAnnotationPresent(Rollback.class);
	}

	protected void assertEntityEquals(String message, DataSet expected,
			Object entity) {

		EntityReader reader = new EntityReader(entity);
		assertEntityEquals(message, expected, reader.read());
	}

	protected void assertEntityListEquals(String message, DataSet expected,
			List<?> list) {

		EntityListReader reader = new EntityListReader(list);
		assertEntityEquals(message, expected, reader.read());
	}

	protected void assertEntityEquals(String message, DataSet expected,
			DataSet actual) {
		message = message == null ? "" : message;
		for (int i = 0; i < expected.getTableSize(); ++i) {
			assertEquals(message, expected.getTable(i), actual.getTable(i));
		}
	}
}
