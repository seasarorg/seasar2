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
package org.seasar.extension.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.extension.filter.util.RequestDumpUtil;
import org.seasar.framework.util.BooleanConversionUtil;

/**
 * {@link HttpServletRequest}の内容をサーブレットが処理する前後にダンプ出力するフィルタです。
 * 
 * @author manhole
 */
public class RequestDumpFilter implements Filter {

    private static final Log log = LogFactory.getLog(RequestDumpFilter.class);

    private FilterConfig config = null;

    private boolean beforeRequestParameter;

    private boolean afterRequestParameter;

    private boolean beforeRequestAttribute;

    private boolean afterRequestAttribute;

    private boolean beforeCookies;

    private boolean afterCookies;

    private boolean beforeRequestHeader;

    private boolean afterRequestHeader;

    private boolean beforeSessionAttribute;

    private boolean afterSessionAttribute;

    private boolean beforeContextAttribute;

    private boolean afterContextAttribute;

    private boolean afterResponse;

    private static final String INDENT = "  ";

    private static final String LF = System.getProperty("line.separator");

    public void init(final FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        beforeRequestParameter = getBooleanParameter(filterConfig,
                "beforeRequestParameter", true);
        afterRequestParameter = getBooleanParameter(filterConfig,
                "afterRequestParameter", false);
        beforeRequestAttribute = getBooleanParameter(filterConfig,
                "beforeRequestAttribute", true);
        afterRequestAttribute = getBooleanParameter(filterConfig,
                "afterRequestAttribute", true);
        beforeRequestHeader = getBooleanParameter(filterConfig,
                "beforeRequestHeader", true);
        afterRequestHeader = getBooleanParameter(filterConfig,
                "afterRequestHeader", false);
        beforeContextAttribute = getBooleanParameter(filterConfig,
                "beforeContextAttribute", true);
        afterContextAttribute = getBooleanParameter(filterConfig,
                "afterContextAttribute", true);
        beforeCookies = getBooleanParameter(filterConfig, "beforeCookies", true);
        afterCookies = getBooleanParameter(filterConfig, "afterCookies", true);
        beforeSessionAttribute = getBooleanParameter(filterConfig,
                "beforeSessionAttribute", true);
        afterSessionAttribute = getBooleanParameter(filterConfig,
                "afterSessionAttribute", true);
        afterResponse = getBooleanParameter(filterConfig, "afterResponse", true);

        final StringBuffer sb = new StringBuffer();
        RequestDumpUtil.dumpContextProperties(sb, filterConfig
                .getServletContext(), LF, INDENT);
        log.debug(sb.toString());
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (config == null) {
            return;
        }
        if (!(request instanceof HttpServletRequest)
                || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        final HttpServletRequest hrequest = (HttpServletRequest) request;
        final HttpServletResponse hresponse = (HttpServletResponse) response;
        dumpBefore(hrequest);
        try {
            chain.doFilter(request, response);
        } finally {
            dumpAfter(hrequest, hresponse);
        }
    }

    private void dumpBefore(final HttpServletRequest request) {
        final ServletContext context = config.getServletContext();
        final StringBuffer sb = new StringBuffer();
        sb.append(LF);
        sb.append(LF);
        sb.append("** before *****************************************: ");
        sb.append(gerServletPath(request));
        sb.append(LF);
        RequestDumpUtil.dumpRequestProperties(sb, request, LF, INDENT);
        RequestDumpUtil.dumpSessionProperties(sb, request, LF, INDENT);
        if (beforeRequestParameter) {
            RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT);
        }
        if (beforeRequestAttribute) {
            RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT);
        }
        if (beforeCookies) {
            RequestDumpUtil.dumpCookies(sb, request, LF, INDENT);
        }
        if (beforeRequestHeader) {
            RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT);
        }
        if (beforeSessionAttribute) {
            RequestDumpUtil.dumpSessionAttributes(sb, request, LF, INDENT);
        }
        if (beforeContextAttribute) {
            RequestDumpUtil.dumpContextAttributes(sb, context, LF, INDENT);
        }
        log.debug(sb.toString());
    }

    private void dumpAfter(final HttpServletRequest request,
            final HttpServletResponse response) {
        final StringBuffer sb = new StringBuffer();
        sb.append(LF);
        sb.append(LF);
        sb.append("** after *****************************************: ");
        sb.append(gerServletPath(request));
        sb.append(LF);
        if (afterResponse) {
            RequestDumpUtil.dumpResponseProperties(sb, response, LF, INDENT);
        }
        if (afterRequestParameter) {
            RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT);
        }
        if (afterRequestAttribute) {
            RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT);
        }
        if (afterCookies) {
            RequestDumpUtil.dumpCookies(sb, request, LF, INDENT);
        }
        if (afterRequestHeader) {
            RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT);
        }
        if (afterSessionAttribute) {
            RequestDumpUtil.dumpSessionAttributes(sb, request, LF, INDENT);
        }
        if (afterContextAttribute) {
            RequestDumpUtil.dumpContextAttributes(sb, config
                    .getServletContext(), LF, INDENT);
        }
        log.debug(sb.toString());
    }

    private String gerServletPath(final ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getServletPath();
        }
        return "";
    }

    public String toString() {
        if (config == null) {
            return ("RequestDumpFilter()");
        }
        final StringBuffer sb = new StringBuffer("RequestDumpFilter(");
        sb.append(config);
        sb.append(")");
        return (sb.toString());
    }

    private boolean getBooleanParameter(final FilterConfig filterConfig,
            final String name, final boolean defaultValue) {
        final String value = filterConfig.getInitParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return BooleanConversionUtil.toPrimitiveBoolean(value);
    }

}
