package org.seasar.framework.ejb.unit;

/**
 * @author taedium
 *
 */
public interface PersistentStateDesc {
	
	String getTableName();
	
	String getColumnName();

	String getStateName();

	Class<?> getPersistentStateType();

	boolean isCollection();
	
	Class<?> getCollectionType();

	Object getValue(Object target);

	void setValue(Object target, Object value);

	boolean isEmbedded();

	boolean isRelationship();
	
	boolean isPersistent();	
	
	boolean isProperty();
	
	PersistentClassDesc createPersistentClass();
}
