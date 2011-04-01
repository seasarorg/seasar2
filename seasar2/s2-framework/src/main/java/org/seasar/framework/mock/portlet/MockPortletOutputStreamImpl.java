/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.mock.portlet;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * {@link MockPortletOutputStream}の実装クラスです。
 * 
 * @author shot
 */
public class MockPortletOutputStreamImpl extends MockPortletOutputStream {

    private PrintWriter writer;

    /**
     * {@link MockPortletOutputStreamImpl}を作成します。
     * 
     * @param writer
     */
    public MockPortletOutputStreamImpl(PrintWriter writer) {
        setPrintWriter(writer);
    }

    public PrintWriter getPrintWriter() {
        return writer;
    }

    public void setPrintWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void write(int b) throws IOException {
        writer.write(b);
    }

    public String toString() {
        if (writer != null) {
            return writer.toString();
        }
        return super.toString();
    }
}