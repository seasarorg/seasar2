/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.exception;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author higa
 * 
 */
public final class SAXRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = -4933312103385038765L;

    private String path;

    public SAXRuntimeException(SAXException cause) {
        this(cause, null);
    }

    public SAXRuntimeException(SAXException cause, String path) {
        super("ESSR0054", createArgs(cause, path), cause);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    protected static Object[] createArgs(SAXException cause, String path) {
        return new Object[] { createMessage(cause, path) };
    }

    protected static String createMessage(final SAXException cause, String path) {
        StringBuffer buf = new StringBuffer(100);
        buf.append(cause);
        if (cause instanceof SAXParseException) {
            SAXParseException e = (SAXParseException) cause;
            if (path != null) {
                buf.append(" at ").append(path);
            } else if (e.getSystemId() != null) {
                buf.append(" at ").append(e.getSystemId());
            }
            final int lineNumber = e.getLineNumber();
            final int columnNumber = e.getColumnNumber();
            buf.append("( lineNumber = ").append(lineNumber).append(
                    ", columnNumber = ").append(columnNumber).append(")");
        }
        return new String(buf);
    }
}
