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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.net.URL;

import org.seasar.framework.util.ResourceUtil;

import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;

/**
 * リソースを扱う{@link TemplateLoader}の実装クラスです。
 * <p>
 * JARファイルに含まれたリソースを扱えます。
 * </p>
 * 
 * @author taedium
 */
public class ResourceTemplateLoader extends URLTemplateLoader {

    /** ベースとなるパス */
    protected String basePath;

    /**
     * インスタンスを構築します。
     * 
     * @param basePath
     *            ベースとなるパス
     */
    public ResourceTemplateLoader(String basePath) {
        if (basePath == null) {
            throw new NullPointerException("basePath");
        }
        this.basePath = basePath;
    }

    @Override
    protected URL getURL(String name) {
        String path = basePath + "/" + name;
        return ResourceUtil.getResourceNoException(path);
    }

}
