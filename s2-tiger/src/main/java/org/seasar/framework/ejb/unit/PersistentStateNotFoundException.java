package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class PersistentStateNotFoundException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private Class targetClass;

    private String persistentStateName;

    private boolean property;

    public PersistentStateNotFoundException(Class targetClass,
            String persistentStateName, boolean property) {
        super(property ? "ESSR0503" : "ESSR0502", new Object[] {
                targetClass.getName(), persistentStateName });
        this.targetClass = targetClass;
        this.persistentStateName = persistentStateName;
        this.property = property;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public String getPersistentStateName() {
        return persistentStateName;
    }

    public boolean isProperty() {
        return property;
    }
}
