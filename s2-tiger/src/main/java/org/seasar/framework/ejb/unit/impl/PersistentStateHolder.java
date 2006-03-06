package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 *
 */
class PersistentStateHolder {
	
	private final Class<?> holderType;
	
	private final String primaryTableName;

	private final boolean propertyAccessed;
	
	private List<PersistentState> persistentStateCache = new ArrayList<PersistentState>();

	private Map<String, PersistentState> persistentStateCacheByName = new HashMap<String, PersistentState>();

	PersistentStateHolder(Class holderType, String primaryTableName, boolean propertyAccessed) {
		if (holderType == null) {
			throw new EmptyRuntimeException("holderType");
		}
		this.holderType = holderType;
		this.primaryTableName = primaryTableName;
		this.propertyAccessed = propertyAccessed;
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(holderType);
		setupPersistenceState(beanDesc);
	}
	
	private void setupPersistenceState(BeanDesc beanDesc) {
		for (int i = 0; i < beanDesc.getFieldSize(); i++) {
			Field field = beanDesc.getField(i);
			PersistentState ps = null;
			if (propertyAccessed) {
				if (beanDesc.hasPropertyDesc(field.getName())) {
					PropertyDesc pd = beanDesc.getPropertyDesc(field.getName());
					ps = new PersistentProperty(pd, primaryTableName);
				} else {
					continue;
				}
			} else {
				ps = new PersistentField(field, primaryTableName);
			}
			if (ps != null) {
				persistentStateCache.add(ps);
				persistentStateCacheByName.put(ps.getStateName(), ps);
			}
		}
	}
	
	Class<?> PersistentStateHolderType() {
		return holderType;
	}
	
	int getPersistentStateSize() {
		return persistentStateCache.size();
	}

	PersistentState getPersistentState(int index) {
		return persistentStateCache.get(index);
	}

	PersistentState getPersistentState(String entityStateName) {
		return persistentStateCacheByName.get(entityStateName);
	}

	boolean isPropertyAccessed() {
		return propertyAccessed;
	}
}
