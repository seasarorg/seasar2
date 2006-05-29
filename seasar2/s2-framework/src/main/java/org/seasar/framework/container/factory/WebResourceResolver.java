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
package org.seasar.framework.container.factory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.URLUtil;

/**
 * @author koichik
 */
public class WebResourceResolver implements ResourceResolver {

    protected ResourceResolver parent;

    public WebResourceResolver() {
        this(new ClassPathResourceResolver());
    }

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
            URL url = servletContext.getResource(path);
            if (url == null) {
                final StringBuffer buf = new StringBuffer(path.length() + 10);
                buf.append("/WEB-INF");
                if (!path.startsWith("/")) {
                    buf.append("/");
                }
                buf.append(path);
                url = servletContext.getResource(new String(buf));
            }
            if (url == null) {
                return null;
            }
            return URLUtil.openStream(url);
        } catch (final MalformedURLException e) {
            return null;
        }
    }
}
