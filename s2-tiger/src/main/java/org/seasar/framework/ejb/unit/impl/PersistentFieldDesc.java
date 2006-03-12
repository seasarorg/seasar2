package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.FieldUtil;

/**
 * @author taedium
 *
 */
public class PersistentFieldDesc extends AbstractPersistentStateDesc implements
		PersistentStateDesc {

	private final Field field;

	public PersistentFieldDesc(Field field, String primaryTableName) {
		super(field.getName(), field.getType(), primaryTableName);
		if (field == null) {
			throw new EmptyRuntimeException("field");
		}
		this.field = field;
		if (Collection.class.isAssignableFrom(field.getType())) {
			Type type = field.getGenericType();
			this.collectionType = extractCollectionType(type);
		}
		introspection(field);
	}

	@Override
	public Object getValue(Object target) {
		return FieldUtil.get(field, target);
	}

	@Override
	public void setValue(Object target, Object value) {
		FieldUtil.set(field, target, value);
	}
	
	@Override
	public boolean isProperty() {
		return false;
	}
}
