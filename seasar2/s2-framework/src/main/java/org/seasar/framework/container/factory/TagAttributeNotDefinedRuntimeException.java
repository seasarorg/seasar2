/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.factory;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author higa
 * 
 */
public final class TagAttributeNotDefinedRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = -1635401319074240988L;

    private String tagName;

    private String attributeName;

    public TagAttributeNotDefinedRuntimeException(String tagName,
            String attributeName) {

        super("ESSR0056", new Object[] { tagName, attributeName });
        this.tagName = tagName;
        this.attributeName = attributeName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
