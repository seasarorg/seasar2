/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * リクエストのエンコーディングを設定するためのフィルタです。
 * 
 * @author higa
 * 
 */
public class EncodingFilter implements Filter {

    /**
     * エンコーディングのキー
     */
    public static String ENCODING = "encoding";

    /**
     * デフォルトのエンコーディング
     */
    public static String DEFAULT_ENCODING = "Windows-31j";

    private String encoding;

    /**
     * {@link EncodingFilter}を作成します。
     */
    public EncodingFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter(ENCODING);
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }
}