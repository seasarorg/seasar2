/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.mock.servlet;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link MockServletInputStream}の実装クラスです。
 * 
 * @author Jun Futagawa
 */
public class MockServletInputStreamImpl extends MockServletInputStream {

    private final InputStream sourceStream;

    /**
     * {@link MockServletInputStreamImpl}を作成します。
     * 
     * @param sourceStream
     */
    public MockServletInputStreamImpl(final InputStream sourceStream) {
        this.sourceStream = sourceStream;
    }

    /**
     * sourceStreamを返します。
     * 
     * @return
     */
    public final InputStream getSourceStream() {
        return this.sourceStream;
    }

    /**
     * @see java.io.InputStream#read()
     */
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    /**
     * @see java.io.InputStream#close()
     */
    public void close() throws IOException {
        super.close();
        this.sourceStream.close();
    }

}
