package org.seasar.framework.ejb.unit;

/**
 * @author taedium
 * 
 */
public interface PersistentClassDesc {

    String getName();

    int getStateDescSize();

    PersistentStateDesc getStateDesc(int index);

    PersistentStateDesc getStateDesc(String entityStateName)
            throws PersistentStateNotFoundException;

    boolean isPropertyAccessed();

    Class<?> getPersistentClass();

    int getTableSize();

    String getTableName(int index);
}
