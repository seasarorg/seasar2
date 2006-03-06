package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 *
 */
public class AbstractPersistentClass implements PersistentClass {

	protected final Class<?> holderType;

	protected String name;

	protected List<String> tableNames = new ArrayList<String>();

	protected List<PersistentState> persistentStateCache = new ArrayList<PersistentState>();

	protected Map<String, PersistentState> persistentStateCacheByName = new HashMap<String, PersistentState>();

	protected boolean propertyAccessed;

	protected AbstractPersistentClass(Class holderType) {
		if (holderType == null) {
			throw new EmptyRuntimeException("holderType");
		}
		this.holderType = holderType;
	}

	protected void setupPersistenceState(BeanDesc beanDesc) {
		for (int i = 0; i < beanDesc.getFieldSize(); i++) {
			Field field = beanDesc.getField(i);
			PersistentState ps = null;
			if (propertyAccessed) {
				if (beanDesc.hasPropertyDesc(field.getName())) {
					PropertyDesc pd = beanDesc.getPropertyDesc(field.getName());
					ps = new PersistentProperty(pd, tableNames.get(0));
				} else {
					continue;
				}
			} else {
				ps = new PersistentField(field, tableNames.get(0));
			}
			if (ps != null) {
				persistentStateCache.add(ps);
				persistentStateCacheByName.put(ps.getStateName(), ps);
			}
		}
	}

	public String getName() {
		return name;
	}

	public Class<?> getPersistentClassType() {
		return holderType;
	}

	public PersistentState getPersistentState(int index) {
		return persistentStateCache.get(index);
	}

	public PersistentState getPersistentState(String persistentStateName) {
		if (persistentStateCacheByName.containsKey(persistentStateName)) {
			return persistentStateCacheByName.get(persistentStateName);
		}
		if (propertyAccessed) {
			throw new PropertyNotFoundRuntimeException(holderType,
					persistentStateName);

		} else {
			throw new FieldNotFoundRuntimeException(holderType,
					persistentStateName);
		}
	}

	public int getPersistentStateSize() {
		return persistentStateCache.size();
	}

	public String getTableName(int index) {
		return tableNames.get(index);
	}

	public int getTableSize() {
		return tableNames.size();
	}

	public boolean isPropertyAccessed() {
		return propertyAccessed;
	}
}
