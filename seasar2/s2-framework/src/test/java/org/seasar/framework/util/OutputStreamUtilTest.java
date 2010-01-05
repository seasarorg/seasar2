/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.seasar.framework.exception.IORuntimeException;

/**
 * @author shot
 */
public class OutputStreamUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testClose() throws Exception {
        NotifyOutputStream out = new NotifyOutputStream();
        OutputStreamUtil.close(out);
        assertEquals("closed", out.getNotify());
    }

    /**
     * @throws Exception
     */
    public void testCloseNull() throws Exception {
        OutputStreamUtil.close((OutputStream) null);
        assertTrue(true);
    }

    /**
     * @throws Exception
     */
    public void testClose_throwIOException() throws Exception {
        OutputStream out = new IOExceptionOccurOutputStream();
        try {
            OutputStreamUtil.close(out);
            fail();
        } catch (IORuntimeException e) {
            assertTrue(true);
        }
    }

    private static class NotifyOutputStream extends OutputStream {
        private String notify_;

        public void write(int arg0) throws IOException {
        }

        public void close() throws IOException {
            super.close();
            notify_ = "closed";
        }

        /**
         * @return
         */
        public String getNotify() {
            return notify_;
        }
    }

    private static class IOExceptionOccurOutputStream extends OutputStream {

        public void write(int arg0) throws IOException {
        }

        public void close() throws IOException {
            throw new IOException();
        }

    }
}
