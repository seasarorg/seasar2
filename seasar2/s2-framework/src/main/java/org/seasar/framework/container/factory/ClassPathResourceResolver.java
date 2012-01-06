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
package org.seasar.framework.container.factory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.seasar.framework.env.Env;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.URLUtil;

/**
 * クラスパス経由でリソースを取得する{@link ResourceResolver ResourceResolver}の実装クラスです。
 * 
 * @author koichik
 * @author skirnir
 * @author azusa
 */
public class ClassPathResourceResolver implements ResourceResolver {

    private static final char COLON = ':';

    /**
     * <code>ClassPathResourceResolver</code>を構築します。
     */
    public ClassPathResourceResolver() {
    }

    public InputStream getInputStream(final String path) {
        URL url = getURL(path);
        if (url == null) {
            return null;
        }
        return URLUtil.openStream(url);
    }

    /**
     * クラスパスから読み込み対象となるリソースを取得し、URLを構築します。 取得する際には、拡張子の手前に環境名をサフィックスを加えたパス(例
     * env.txt→env_ut.txt)を用います。 環境名を加えたパスのリソースが存在しない場合は、パスをそのまま用います。
     * 
     * @param path
     *            読み込み対象となるリソースのパス
     * @return 取得したリソースのURL
     * @see Env#adjustPath(String)
     */
    protected URL getURL(final String path) {
        String extPath = Env.adjustPath(path);
        URL url = toURL(extPath);
        if (url == null && !extPath.equals(path)) {
            url = toURL(path);
        }
        return url;
    }

    URL toURL(final String path) {
        if (path.indexOf(COLON) >= 0) {
            try {
                return new URL(path);
            } catch (MalformedURLException ignore) {
            }
        }
        return ResourceUtil.getResourceNoException(path);
    }
}
