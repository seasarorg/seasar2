package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 *
 */
public class PersistentStateDescNotFoundException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private Class targetClass;

    private String propertyName;

    public PersistentStateDescNotFoundException(Class targetClass,
            String stateName) {
        // TODO : write massage to SSRMessages.properties
        super("ESSR0503", new Object[] { targetClass.getName(), stateName });
        this.targetClass = targetClass;
        this.propertyName = stateName;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
