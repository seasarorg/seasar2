package org.seasar.framework.ejb.unit;

/**
 * @author taedium
 *
 */
public interface PersistentState {
	
	boolean hasTableName();
	
	String getTableName();

	boolean hasColumnName();
	
	String getColumnName();

	String getStateName();

	Class<?> getStateType();

	boolean isCollection();
	
	Class<?> getCollectionType();

	Object getValue(Object target);

	void setValue(Object target, Object value);

	boolean isEmbedded();

	boolean isRelationship();
	
	boolean isPersistent();	
	
	boolean isProperty();
	
	PersistentClass createPersistentClass();
}
