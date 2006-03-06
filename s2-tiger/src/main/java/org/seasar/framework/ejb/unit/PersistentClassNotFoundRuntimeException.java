package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 *
 */
public class PersistentClassNotFoundRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    public PersistentClassNotFoundRuntimeException(Class targetClass) {
        // TODO : write message to SSRMessages.properties
        super("ESSR0502", new Object[] { targetClass.getName() });
    }
}
