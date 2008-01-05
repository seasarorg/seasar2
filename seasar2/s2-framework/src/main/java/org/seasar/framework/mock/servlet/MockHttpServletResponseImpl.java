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
package org.seasar.framework.mock.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.util.EmptyEnumeration;
import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.SPrintWriter;

/**
 * {@link MockHttpServletResponse}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class MockHttpServletResponseImpl implements MockHttpServletResponse {

    private List cookieList;

    private Map headers = new HashMap();

    private boolean committed = false;

    private int status;

    private String message;

    private byte[] buffer = new byte[1024];

    private Locale locale;

    private String characterEncoding;

    private PrintWriter writer = new SPrintWriter();

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private ServletOutputStream outputStream = new MockServletOutputStreamImpl(
            byteArrayOutputStream);

    private boolean getWriterCalled;

    private boolean getOutputStreamCalled;

    /**
     * {@link MockHttpServletResponseImpl}を作成します。
     * 
     * @param request
     */
    public MockHttpServletResponseImpl(HttpServletRequest request) {
        cookieList = new ArrayList(Arrays.asList(request.getCookies()));
        locale = request.getLocale();
        characterEncoding = request.getCharacterEncoding();
    }

    public Cookie[] getCookies() {
        return (Cookie[]) cookieList.toArray(new Cookie[cookieList.size()]);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String url) {
        return url;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String url) {
        return url;
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String url) {
        return encodeRedirectUrl(url);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int,
     *      java.lang.String)
     */
    public void sendError(int status, String message) throws IOException {
        setStatus(status, message);
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int status) throws IOException {
        setStatus(status);
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String path) throws IOException {
    }

    public Enumeration getHeaders(String name) {
        List values = getHeaderList(name);
        if (values != null) {
            return new EnumerationAdapter(values.iterator());
        }
        return new EmptyEnumeration();
    }

    public String getHeader(String name) {
        List values = getHeaderList(name);
        if (values != null) {
            return (String) values.get(0);
        }
        return null;
    }

    public Enumeration getHeaderNames() {
        return new EnumerationAdapter(headers.keySet().iterator());
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String,
     *      long)
     */
    public void setDateHeader(String name, long value) {
        setHeader(name, MockHeaderUtil.getDateValue(value));

    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String,
     *      long)
     */
    public void addDateHeader(String name, long value) {
        addHeader(name, MockHeaderUtil.getDateValue(value));
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String,
     *      java.lang.String)
     */
    public void setHeader(String name, String value) {
        List values = new ArrayList();
        values.add(value);
        headers.put(name.toLowerCase(), values);
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String,
     *      java.lang.String)
     */
    public void addHeader(String name, String value) {
        List values = getHeaderList(name);
        if (values == null) {
            values = new ArrayList();
        }
        values.add(value);
        headers.put(name.toLowerCase(), values);
    }

    public int getIntHeader(String name) {
        String value = getHeader(name);
        return MockHeaderUtil.getIntValue(value);
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String,
     *      int)
     */
    public void setIntHeader(String name, int value) {
        setHeader(name, value + "");

    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String,
     *      int)
     */
    public void addIntHeader(String name, int value) {
        addHeader(name, value + "");
    }

    private List getHeaderList(String name) {
        name = name.toLowerCase();
        return (List) headers.get(name);
    }

    /**
     * 
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int status) {
        setStatus(status, getResponseStatusMessage(status));

    }

    private static String getResponseStatusMessage(int status) {
        switch (status) {
        case HttpServletResponse.SC_OK:
            return "OK";
        case HttpServletResponse.SC_ACCEPTED:
            return "Accepted";
        case HttpServletResponse.SC_BAD_GATEWAY:
            return "Bad Gateway";
        case HttpServletResponse.SC_BAD_REQUEST:
            return "Bad Request";
        case HttpServletResponse.SC_CONFLICT:
            return "Conflict";
        case HttpServletResponse.SC_CONTINUE:
            return "Continue";
        case HttpServletResponse.SC_CREATED:
            return "Created";
        case HttpServletResponse.SC_EXPECTATION_FAILED:
            return "Expectation Failed";
        case HttpServletResponse.SC_FORBIDDEN:
            return "Forbidden";
        case HttpServletResponse.SC_GATEWAY_TIMEOUT:
            return "Gateway Timeout";
        case HttpServletResponse.SC_GONE:
            return "Gone";
        case HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED:
            return "HTTP Version Not Supported";
        case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
            return "Internal Server Error";
        case HttpServletResponse.SC_LENGTH_REQUIRED:
            return "Length Required";
        case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
            return "Method Not Allowed";
        case HttpServletResponse.SC_MOVED_PERMANENTLY:
            return "Moved Permanently";
        case HttpServletResponse.SC_MOVED_TEMPORARILY:
            return "Moved Temporarily";
        case HttpServletResponse.SC_MULTIPLE_CHOICES:
            return "Multiple Choices";
        case HttpServletResponse.SC_NO_CONTENT:
            return "No Content";
        case HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION:
            return "Non-Authoritative Information";
        case HttpServletResponse.SC_NOT_ACCEPTABLE:
            return "Not Acceptable";
        case HttpServletResponse.SC_NOT_FOUND:
            return "Not Found";
        case HttpServletResponse.SC_NOT_IMPLEMENTED:
            return "Not Implemented";
        case HttpServletResponse.SC_NOT_MODIFIED:
            return "Not Modified";
        case HttpServletResponse.SC_PARTIAL_CONTENT:
            return "Partial Content";
        case HttpServletResponse.SC_PAYMENT_REQUIRED:
            return "Payment Required";
        case HttpServletResponse.SC_PRECONDITION_FAILED:
            return "Precondition Failed";
        case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
            return "Proxy Authentication Required";
        case HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE:
            return "Request Entity Too Large";
        case HttpServletResponse.SC_REQUEST_TIMEOUT:
            return "Request Timeout";
        case HttpServletResponse.SC_REQUEST_URI_TOO_LONG:
            return "Request URI Too Long";
        case HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
            return "Requested Range Not Satisfiable";
        case HttpServletResponse.SC_RESET_CONTENT:
            return "Reset Content";
        case HttpServletResponse.SC_SEE_OTHER:
            return "See Other";
        case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
            return "Service Unavailable";
        case HttpServletResponse.SC_SWITCHING_PROTOCOLS:
            return "Switching Protocols";
        case HttpServletResponse.SC_UNAUTHORIZED:
            return "Unauthorized";
        case HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE:
            return "Unsupported Media Type";
        case HttpServletResponse.SC_USE_PROXY:
            return "Use Proxy";
        case 207:
            return "Multi-Status";
        case 422:
            return "Unprocessable Entity";
        case 423:
            return "Locked";
        case 507:
            return "Insufficient Storage";
        default:
            return "HTTP Response Status " + status;
        }
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletResponse#setStatus(int,
     *      java.lang.String)
     */
    public void setStatus(int status, String message) {
        assertNotCommitted();
        this.status = status;
        this.message = message;
        resetBuffer();
    }

    private void assertNotCommitted() {
        if (isCommitted()) {
            throw new IllegalStateException("Already committed");
        }
    }

    /**
     * 
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * 
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException {
        if (getWriterCalled) {
            throw new IllegalStateException();
        }
        if (!getOutputStreamCalled) {
            getOutputStreamCalled = true;
        }
        return outputStream;
    }

    /**
     * 
     * @see javax.servlet.ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws IOException {
        if (getOutputStreamCalled) {
            throw new IllegalStateException();
        }
        if (!getWriterCalled) {
            getWriterCalled = true;
        }
        return writer;
    }

    /**
     * 
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int contentLength) {
        setIntHeader("content-length", contentLength);
    }

    public int getContentLength() {
        return getIntHeader("content-length");
    }

    public String getContentType() {
        return getHeader("content-type");
    }

    /**
     * 
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String contentType) {
        setHeader("content-type", contentType);

    }

    /**
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int size) {
        assertNotCommitted();
        if (size <= buffer.length) {
            return;
        }
        buffer = new byte[size];

    }

    /**
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize() {
        return buffer.length;
    }

    /**
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException {
    }

    /**
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer() {
        assertNotCommitted();
    }

    /**
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset() {
        committed = false;
    }

    /**
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale() {
        return locale;
    }

    public byte[] getResponseBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    public String getResponseString() {
        return writer.toString();
    }

}
