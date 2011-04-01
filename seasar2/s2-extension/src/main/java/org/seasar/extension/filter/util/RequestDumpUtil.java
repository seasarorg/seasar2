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
package org.seasar.extension.filter.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpServletRequest}の内容を編集するユーティリティです。
 * 
 * @author manhole
 */
public class RequestDumpUtil {

    /**
     * リクエストヘッダの内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpRequestHeaders(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        for (final Iterator it = toSortedSet(request.getHeaderNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            final String value = request.getHeader(name);
            sb.append(indent);
            sb.append("[header]").append(name);
            sb.append("=").append(value);
            sb.append(lf);
        }
    }

    /**
     * コンテキスト属性の内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param context
     *            コンテキスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpContextAttributes(final StringBuffer sb,
            final ServletContext context, final String lf, final String indent) {
        if (context == null) {
            return;
        }
        for (final Iterator it = toSortedSet(context.getAttributeNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            final Object attr = context.getAttribute(name);
            sb.append(indent);
            sb.append("[context]").append(name).append("=").append(attr);
            sb.append(lf);
        }
    }

    /**
     * クッキーの内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpCookies(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        final Cookie cookies[] = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (int i = 0; i < cookies.length; i++) {
            sb.append(indent);
            sb.append("[cookie]").append(cookies[i].getName());
            sb.append("=").append(cookies[i].getValue());
            sb.append(lf);
        }
    }

    /**
     * リクエスト属性の内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpRequestAttributes(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        for (final Iterator it = toSortedSet(request.getAttributeNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            final Object attr = request.getAttribute(name);
            sb.append(indent);
            sb.append("[request]").append(name).append("=").append(attr);
            sb.append(lf);
        }
    }

    /**
     * セッション属性の内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpSessionAttributes(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        for (final Iterator it = toSortedSet(session.getAttributeNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            final Object attr = session.getAttribute(name);
            sb.append(indent);
            sb.append("[session]").append(name).append("=").append(attr);
            sb.append(lf);
        }
    }

    private static SortedSet toSortedSet(final Enumeration enu) {
        final SortedSet set = new TreeSet();
        set.addAll(Collections.list(enu));
        return set;
    }

    /**
     * リクエストパラメータの内容を文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpRequestParameters(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        for (final Iterator it = toSortedSet(request.getParameterNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            sb.append(indent);
            sb.append("[param]").append(name).append("=");
            final String values[] = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(values[i]);
            }
            sb.append(lf);
        }
    }

    /**
     * リクエストのプロパティを文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpRequestProperties(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {

        sb.append(indent);
        sb.append("Request class=" + request.getClass().getName()).append(
                ", instance=").append(request.toString().trim());

        sb.append(lf);
        sb.append(indent);

        sb.append("RequestedSessionId=")
                .append(request.getRequestedSessionId());

        sb.append(lf);
        sb.append(indent);

        sb.append("REQUEST_URI=").append(request.getRequestURI());
        sb.append(", SERVLET_PATH=").append(request.getServletPath());

        sb.append(lf);
        sb.append(indent);
        sb.append("CharacterEncoding=" + request.getCharacterEncoding());
        sb.append(", ContentLength=").append(request.getContentLength());
        sb.append(", ContentType=").append(request.getContentType());
        sb.append(", Locale=").append(request.getLocale());
        sb.append(", Locales=");
        final Enumeration locales = request.getLocales();
        boolean first = true;
        while (locales.hasMoreElements()) {
            final Locale locale = (Locale) locales.nextElement();
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(locale.toString());
        }
        sb.append(", Scheme=").append(request.getScheme());
        sb.append(", isSecure=").append(request.isSecure());

        sb.append(lf).append(indent);

        sb.append("SERVER_PROTOCOL=").append(request.getProtocol());
        sb.append(", REMOTE_ADDR=").append(request.getRemoteAddr());
        sb.append(", REMOTE_HOST=").append(request.getRemoteHost());
        sb.append(", SERVER_NAME=").append(request.getServerName());
        sb.append(", SERVER_PORT=").append(request.getServerPort());

        sb.append(lf).append(indent);

        sb.append("ContextPath=").append(request.getContextPath());
        sb.append(", REQUEST_METHOD=").append(request.getMethod());
        sb.append(", QUERY_STRING=").append(request.getQueryString());
        sb.append(", PathInfo=").append(request.getPathInfo());
        sb.append(", RemoteUser=").append(request.getRemoteUser());

        sb.append(lf);

    }

    /**
     * コンテキストのプロパティを文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param context
     *            コンテキスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpContextProperties(final StringBuffer sb,
            final ServletContext context, final String lf, final String indent) {
        sb.append(indent);
        sb.append("ContextRealPath=").append(context.getRealPath("/"));

        sb.append(lf).append(indent);

        sb.append("SERVER_SOFTWARE=").append(context.getServerInfo());
        sb.append(", ServletContextName=").append(
                context.getServletContextName());
        sb.append(", MajorVersion=").append(context.getMajorVersion());
        sb.append(", MinorVersion=").append(context.getMinorVersion());
    }

    /**
     * セッションのプロパティを文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param request
     *            リクエスト
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpSessionProperties(final StringBuffer sb,
            final HttpServletRequest request, final String lf,
            final String indent) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        sb.append(indent);
        sb.append("Session SessionId=").append(session.getId());
        sb.append(lf).append(indent);
        sb.append("Session :: CreationTime=").append(session.getCreationTime());
        sb.append(", LastAccessedTime=").append(session.getLastAccessedTime());
        sb.append(", MaxInactiveInterval=").append(
                session.getMaxInactiveInterval());
        sb.append(lf);
    }

    /**
     * レスポンスのプロパティを文字列バッファに編集します。
     * 
     * @param sb
     *            文字列バッファ
     * @param response
     *            レスポンス
     * @param lf
     *            改行文字
     * @param indent
     *            インデント
     */
    public static void dumpResponseProperties(final StringBuffer sb,
            final HttpServletResponse response, final String lf,
            final String indent) {
        sb.append(indent);
        sb.append("Response class=" + response.getClass().getName()).append(
                ", instance=").append(response.toString().trim());
        sb.append(lf);
    }

}
