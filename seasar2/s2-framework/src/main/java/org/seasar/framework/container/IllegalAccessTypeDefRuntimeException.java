package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

public class IllegalAccessTypeDefRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private String accessTypeName;

    public IllegalAccessTypeDefRuntimeException(final String messageCode) {
        super("ESSR0083", new Object[] { messageCode });
    }

    public String getAccessTypeName() {
        return accessTypeName;
    }
}
