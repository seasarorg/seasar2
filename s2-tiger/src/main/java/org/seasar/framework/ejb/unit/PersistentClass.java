package org.seasar.framework.ejb.unit;

/**
 * @author taedium
 *
 */
public interface PersistentClass {
	
    String getName();
	
	int getPersistentStateSize();

	PersistentState getPersistentState(int index);

	PersistentState getPersistentState(String entityStateName);

	boolean isPropertyAccessed();

	Class<?> getPersistentClassType();

	int getTableSize();

	String getTableName(int index);
}
