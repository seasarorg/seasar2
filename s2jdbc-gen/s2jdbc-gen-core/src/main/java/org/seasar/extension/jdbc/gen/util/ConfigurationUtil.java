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
package org.seasar.extension.jdbc.gen.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.exception.IORuntimeException;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * {@link Configuration}のユーティリティークラスです。
 * 
 * @author taedium
 */
public class ConfigurationUtil {

    /**
     * 
     */
    protected ConfigurationUtil() {
    }

    /**
     * テンプレートファイルを格納するディレクトリを設定します。
     * 
     * @param configuration
     *            {@link Configuration}
     * @param dirs
     *            テンプレートファイルを格納するディレクトリの可変長配列
     */
    public static void setDirectoriesForTemplateLoading(
            Configuration configuration, File... dirs) {
        if (dirs == null) {
            throw new NullPointerException("dirs");
        }
        List<FileTemplateLoader> loaderList = new ArrayList<FileTemplateLoader>();
        try {
            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i] != null) {
                    loaderList.add(new FileTemplateLoader(dirs[i]));
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        TemplateLoader loader = new MultiTemplateLoader(loaderList
                .toArray(new FileTemplateLoader[loaderList.size()]));
        configuration.setTemplateLoader(loader);
    }

    /**
     * {@link Template}を取得します。
     * 
     * @param configuration
     *            {@link Configuration}
     * @param name
     *            テンプレートの名前
     * @return {@link Template}
     */
    public static Template getTemplate(Configuration configuration, String name) {
        try {
            return configuration.getTemplate(name);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
