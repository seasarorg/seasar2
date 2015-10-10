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
package org.seasar.framework.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * {@link ResourceBundle}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class ResourceBundleUtil {

    /**
     * インスタンスを構築します。
     */
    protected ResourceBundleUtil() {
    }

    /**
     * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
     * 
     * @param name
     * @param locale
     * @return {@link ResourceBundle}
     * @see ResourceBundle#getBundle(String, Locale)
     */
    public static final ResourceBundle getBundle(String name, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            return ResourceBundle.getBundle(name, locale);
        } catch (MissingResourceException ignore) {
            return null;
        }
    }

    /**
     * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
     * 
     * @param name
     * @param locale
     * @param classLoader
     * @return {@link ResourceBundle}
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     */
    public static final ResourceBundle getBundle(String name, Locale locale,
            ClassLoader classLoader) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            return ResourceBundle.getBundle(name, locale, classLoader);
        } catch (MissingResourceException ignore) {
            return null;
        }
    }

    /**
     * {@link Map}に変換します。
     * 
     * @param bundle
     * @return {@link Map}
     */
    public static final Map convertMap(ResourceBundle bundle) {
        Map ret = new HashMap();
        for (Enumeration e = bundle.getKeys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = bundle.getString(key);
            ret.put(key, value);
        }
        return ret;
    }

    /**
     * {@link Map}に変換します。
     * 
     * @param name
     * @param locale
     * @return {@link Map}
     */
    public static final Map convertMap(String name, Locale locale) {
        ResourceBundle bundle = getBundle(name, locale);
        return convertMap(bundle);
    }
}