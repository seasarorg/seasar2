package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

public class IllegalAccessTypeDefRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private String accessTypeName;

    public IllegalAccessTypeDefRuntimeException(final String accessTypeName) {
        super("ESSR0083", new Object[] { accessTypeName });
        this.accessTypeName = accessTypeName;
    }

    public String getAccessTypeName() {
        return accessTypeName;
    }
}
