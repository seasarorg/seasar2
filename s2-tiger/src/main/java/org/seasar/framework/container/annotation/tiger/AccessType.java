package org.seasar.framework.container.annotation.tiger;

public enum AccessType {
    PROPERTY, FIELD;

    public String getName() {
        return toString().toLowerCase();
    }
}
