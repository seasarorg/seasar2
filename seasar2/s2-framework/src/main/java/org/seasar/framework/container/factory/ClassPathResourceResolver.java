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

import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.URLUtil;

/**
 * @author koichik
 * @author skirnir
 */
public class ClassPathResourceResolver implements ResourceResolver {

    private static final char COLON = ':';

    public ClassPathResourceResolver() {
    }

    public InputStream getInputStream(final String path) {
        final URL url = toURL(path);
        if (url == null) {
            return null;
        }
        return URLUtil.openStream(url);
    }

    URL toURL(final String path) {
        if (path.indexOf(COLON) >= 0) {
            try {
                return new URL(path);
            } catch (MalformedURLException ex) {
                return null;
            }
        } else {
            return ResourceUtil.getResourceNoException(path);
        }
    }
}
