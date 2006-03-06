package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 *
 */
public class PersistentProperty extends AbstractPersistentState implements
		PersistentState {

	private final PropertyDesc propertyDesc;

	public PersistentProperty(PropertyDesc propertyDesc, String primaryTableName) {
		
		super(propertyDesc.getPropertyName(), propertyDesc.getPropertyType(),
				primaryTableName);

		if (propertyDesc == null) {
			throw new EmptyRuntimeException("propertyDesc");
		}
		this.propertyDesc = propertyDesc;
		
		if (Collection.class.isAssignableFrom(stateType)) {
			Type type;
			if (propertyDesc.hasReadMethod()) {
				Method m = propertyDesc.getReadMethod();
				type = m.getGenericReturnType();
			} else {
				Method m = propertyDesc.getWriteMethod();
				type = m.getGenericParameterTypes()[0];
			}
			this.collectionType = extractCollectionType(type);
		}
		
		if (propertyDesc.hasReadMethod()) {
			introspection(propertyDesc.getReadMethod());
		} else {
			this.tableName = primaryTableName.toUpperCase();
			this.columnName = stateName.toUpperCase();
		}
	}

	@Override
	public Object getValue(Object target) {
		return propertyDesc.getValue(target);
	}

	@Override
	public void setValue(Object target, Object value) {
		propertyDesc.setValue(target, value);
	}
	
	@Override
	public boolean isProperty() {
		return true;
	}
}
