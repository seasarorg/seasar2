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
package org.seasar.framework.mock.portlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletURL;

import org.seasar.framework.util.SPrintWriter;

/**
 * @author shot
 */
public class MockPortletRenderResponseImpl implements MockPortletRenderResponse {

    private String namespace;

    private String contentType;

    private String characterEncoding;

    private String title;

    private int size;

    private boolean commited;

    private Map valueMap = new HashMap();

    private Map onlyValueMap = new HashMap();

    private PrintWriter writer = new SPrintWriter();

    private MockPortletOutputStream outputStream = new MockPortletOutputStreamImpl(
            writer);

    private Locale locale;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public PortletURL createRenderURL() {
        return null;
    }

    public PortletURL createActionURL() {
        return null;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setBufferSize(int size) {
        this.size = size;
    }

    public int getBufferSize() {
        return size;
    }

    public void flushBuffer() throws IOException {
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return commited;
    }

    public void reset() {
        commited = false;
    }

    public OutputStream getPortletOutputStream() throws IOException {
        return outputStream;
    }

    public void addProperty(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (valueMap.containsKey(key)) {
            List list = (List) valueMap.get(key);
            list.add(value);
        } else {
            List list = new ArrayList();
            list.add(value);
            valueMap.put(key, list);
        }
    }

    public void setProperty(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        onlyValueMap.put(key, value);
    }

    public String encodeURL(String arg0) {
        return null;
    }

}
