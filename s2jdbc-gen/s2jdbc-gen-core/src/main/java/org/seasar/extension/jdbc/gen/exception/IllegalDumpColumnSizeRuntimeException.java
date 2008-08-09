/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class IllegalDumpColumnSizeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    protected String path;

    protected int lineNumber;

    protected int lineColumnSize;

    protected int headerColumnSize;

    public IllegalDumpColumnSizeRuntimeException(String path, int lineNumber,
            int lineColumnSize, int headerColumnSize) {
        super("ES2JDBCGen0012", new Object[] { path, lineNumber, lineColumnSize,
                headerColumnSize });
        this.path = path;
        this.lineNumber = lineNumber;
        this.lineColumnSize = lineColumnSize;
        this.headerColumnSize = headerColumnSize;
    }

    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return Returns the lineNumber.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return Returns the lineColumnSize.
     */
    public int getLineColumnSize() {
        return lineColumnSize;
    }

    /**
     * @return Returns the headerColumnSize.
     */
    public int getHeaderColumnSize() {
        return headerColumnSize;
    }

}
