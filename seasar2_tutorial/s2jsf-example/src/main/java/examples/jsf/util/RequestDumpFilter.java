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
package examples.jsf.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author manhole (ARK Systems Co., Ltd.)
 */
public final class RequestDumpFilter implements Filter {

    private static final Log log = LogFactory.getLog(RequestDumpFilter.class);

    private FilterConfig config = null;

    public void init(final FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }

    public void destroy() {
        config = null;
    }

    private static final String INDENT = "  ";

    // System.getProperty("line.separator");
    private static final String LF = System.getProperty("line.separator");

    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (config == null) {
            return;
        }
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest hrequest = (HttpServletRequest) request;
            log.debug(LF + LF
                    + "** before *****************************************: "
                    + gerServletPath(request) + LF + dumpRequest(hrequest));
            try {
                chain.doFilter(request, response);
            } finally {
                final StringBuffer sb = new StringBuffer();
                RequestDumpUtil.dumpRequestAttributes(sb, hrequest, LF, INDENT);
                RequestDumpUtil.dumpCookies(sb, hrequest, LF, INDENT);
                RequestDumpUtil.dumpSessionAttributes(sb, hrequest, LF, INDENT);
                RequestDumpUtil.dumpContextAttributes(sb, config
                        .getServletContext(), LF, INDENT);
                log
                        .debug(LF
                                + LF
                                + "** after *****************************************: "
                                + gerServletPath(request) + LF + sb.toString());
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private String dumpRequest(final HttpServletRequest request) {
        final StringBuffer sb = new StringBuffer();
        final ServletContext context = config.getServletContext();

        RequestDumpUtil.dumpRequestAndContextProperties(sb, request, context,
                LF, INDENT);
        RequestDumpUtil.dumpRequestParameters(sb, request, LF, INDENT);
        RequestDumpUtil.dumpRequestAttributes(sb, request, LF, INDENT);
        RequestDumpUtil.dumpCookies(sb, request, LF, INDENT);
        RequestDumpUtil.dumpRequestHeaders(sb, request, LF, INDENT);
        RequestDumpUtil.dumpSessionAttributes(sb, request, LF, INDENT);
        RequestDumpUtil.dumpContextAttributes(sb, context, LF, INDENT);

        return sb.toString();
    }

    private String gerServletPath(final ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getServletPath();
        }
        return null;
    }

    public String toString() {
        if (config == null) {
            return ("RequestDumperFilter()");
        }
        final StringBuffer sb = new StringBuffer("RequestDumperFilter(");
        sb.append(config);
        sb.append(")");
        return (sb.toString());
    }

}
