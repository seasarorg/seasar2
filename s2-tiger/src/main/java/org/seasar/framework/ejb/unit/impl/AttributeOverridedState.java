package org.seasar.framework.ejb.unit.impl;

import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 *
 */
public class AttributeOverridedState implements PersistentState {

	private PersistentState original;

	private String columnName;

	private String tableName;

	public AttributeOverridedState(PersistentState original) {
		this.original = original;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Class<?> getCollectionType() {
		return original.getCollectionType();
	}

	public String getColumnName() {
		if (StringUtil.isEmpty(columnName)) {
			return original.getColumnName();
		}
		return columnName;
	}

	public String getStateName() {
		return original.getStateName();
	}

	public Class<?> getStateType() {
		return original.getStateType();
	}

	public String getTableName() {
		if (StringUtil.isEmpty(tableName)) {
			return original.getTableName();
		}
		return tableName;
	}

	public Object getValue(Object target) {
		return original.getValue(target);
	}

	public boolean hasColumnName() {
		return getColumnName() != null;
	}

	public boolean hasTableName() {
		return getTableName() != null;
	}

	public boolean isCollection() {
		return original.isCollection();
	}

	public boolean isEmbedded() {
		return original.isEmbedded();
	}

	public boolean isPersistent() {
		return original.isPersistent();
	}

	public boolean isProperty() {
		return original.isProperty();
	}

	public boolean isRelationship() {
		return original.isRelationship();
	}

	public void setValue(Object target, Object value) {
		original.setValue(target, value); 
	}
	
	public PersistentClass createPersistentClass() {
		return original.createPersistentClass();
	}
}
