/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.env.Env;
import org.seasar.framework.util.URLUtil;

/**
 * Web上のResourceを取得するためのクラスです。
 * <p>
 * {@link javax.servlet.ServletContext#getResource(java.lang.String) }を使ってリソースを読み込みます。
 * 見つからない場合には、親の{@link org.seasar.framework.container.factory.ResourceResolver}に委譲します。
 * </p>
 * 
 * @author koichik
 * @author yatsu
 */
public class WebResourceResolver implements ResourceResolver {

    /**
     * 親となる<code>ResourceResolver</code>です。
     */
    protected ResourceResolver parent;

    /**
     * <code>WebResourceResolver</code>を構築します。
     * <p>
     * 親の<code>ResourceResolver</code>として{@link org.seasar.framework.container.factory.ClassPathResourceResolver}を使います。
     * </p>
     */
    public WebResourceResolver() {
        this(new ClassPathResourceResolver());
    }

    /**
     * <code>WebResourceResolver</code>を構築します。
     * 
     * @param parent
     *            親となる<code>ResourceResolver</code>
     */
    public WebResourceResolver(final ResourceResolver parent) {
        this.parent = parent;
    }

    public InputStream getInputStream(final String path) {
        try {
            if (parent != null) {
                InputStream is = parent.getInputStream(path);
                if (is != null) {
                    return is;
                }
            }
            URL url = getURL(path);
            if (url == null) {
                return null;
            }
            return URLUtil.openStream(url);
        } catch (final MalformedURLException e) {
            return null;
        }
    }

    /**
     * 読み込み対象のリソースのパスからURLを取得します。
     * 
     * @param path
     *            読み込み対象となるリソースのパス
     * @return リソースを指し示すURL
     * @throws MalformedURLException
     *             無効な書式のURLだった場合
     */
    protected URL getURL(final String path) throws MalformedURLException {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        ExternalContext externalContext = container.getExternalContext();
        if (externalContext == null) {
            return null;
        }
        if (!(externalContext.getApplication() instanceof ServletContext)) {
            return null;
        }
        ServletContext servletContext = (ServletContext) externalContext
                .getApplication();
        URL url = getURL(servletContext, path);
        if (url == null) {
            final StringBuffer buf = new StringBuffer(path.length() + 10);
            buf.append("/WEB-INF");
            if (!path.startsWith("/")) {
                buf.append("/");
            }
            buf.append(path);
            String path2 = new String(buf);
            url = getURL(servletContext, path2);
        }
        return url;
    }

    /**
     * 読み込み対象のリソースのパスからURLを取得します。
     * 
     * @param servletContext
     *            サーブレットのコンテキスト情報
     * @param path
     *            読み込み対象となるリソースのパス
     * @return サーブレットのコンテキスト情報から取得されたURL
     * @throws MalformedURLException
     *             無効な書式のURLだった場合
     */
    protected URL getURL(ServletContext servletContext, String path)
            throws MalformedURLException {
        String extPath = Env.adjustPath(path);
        URL url = servletContext.getResource(extPath);
        if (url == null && !extPath.equals(path)) {
            url = servletContext.getResource(path);
        }
        return url;
    }
}
