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
package org.seasar.framework.message;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * {@link MessageResourceBundle}を取得するためのクラスです。
 * 
 * @author shot
 */
public class MessageResourceBundleFactory {

    private static final String PROPERTIES_EXT = ".properties";

    private static final Object NOT_FOUND = new Object();

    private static Map cache = new HashMap();

    private static boolean initialized = false;

    /**
     * {@link MessageResourceBundle}を返します。
     * 
     * @param baseName
     * @return {@link MessageResourceBundle}
     * @see #getBundle(String, Locale)
     */
    public static MessageResourceBundle getBundle(String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    /**
     * {@link MessageResourceBundle}を返します。
     * 
     * @param baseName
     * @param locale
     * @return {@link MessageResourceBundle}
     * @throws ResourceNotFoundRuntimeException
     *             リソースが見つからなかった場合
     */
    public static MessageResourceBundle getBundle(String baseName, Locale locale)
            throws ResourceNotFoundRuntimeException {
        MessageResourceBundle bundle = getNullableBundle(baseName, locale);
        if (bundle != null) {
            return bundle;
        }
        throw new ResourceNotFoundRuntimeException(baseName);
    }

    /**
     * {@link MessageResourceBundle}を返します。 リソースが見つからなかった場合は、 nullを返します。
     * 
     * @param baseName
     * @return {@link MessageResourceBundle}
     * @see #getNullableBundle(String, Locale)
     */
    public static MessageResourceBundle getNullableBundle(String baseName) {
        return getNullableBundle(baseName, Locale.getDefault());
    }

    /**
     * {@link MessageResourceBundle}を返します。 リソースが見つからなかった場合は、 nullを返します。
     * 
     * @param baseName
     * @param locale
     * @return {@link MessageResourceBundle}
     */
    public static MessageResourceBundle getNullableBundle(String baseName,
            Locale locale) {
        AssertionUtil.assertNotNull("baseName", baseName);
        AssertionUtil.assertNotNull("locale", locale);

        String base = baseName.replace('.', '/');

        String[] bundleNames = calcurateBundleNames(base, locale);
        MessageResourceBundleFacade parentFacade = null;
        MessageResourceBundleFacade facade = null;
        int length = bundleNames.length;
        for (int i = 0; i < length; ++i) {
            facade = loadFacade(bundleNames[i] + PROPERTIES_EXT);
            if (parentFacade == null) {
                parentFacade = facade;
            } else if (facade != null) {
                facade.setParent(parentFacade);
                parentFacade = facade;
            }
        }

        if (parentFacade != null) {
            return parentFacade.getBundle();
        } else {
            return null;
        }
    }

    /**
     * メッセージリソースバンドルファザードを返します。
     * 
     * @param path
     *            パス
     * @return メッセージリソースバンドルファザード
     */
    protected static MessageResourceBundleFacade loadFacade(String path) {
        synchronized (cache) {
            if (!initialized) {
                DisposableUtil.add(new Disposable() {
                    public void dispose() {
                        clear();
                        initialized = false;
                    }
                });
                initialized = true;
            }
            Object cachedFacade = cache.get(path);
            if (cachedFacade == NOT_FOUND) {
                return null;
            } else if (cachedFacade != null) {
                return (MessageResourceBundleFacade) cachedFacade;
            }
            URL url = ResourceUtil.getResourceNoException(path);
            if (url != null) {
                MessageResourceBundleFacade facade = new MessageResourceBundleFacade(
                        url);
                cache.put(path, facade);
                return facade;
            } else {
                cache.put(path, NOT_FOUND);
            }
        }
        return null;
    }

    /**
     * リソースバンドル名の配列をロケールから求めて返します。
     * 
     * @param baseName
     *            リソースバンドルの基底名
     * @param locale
     *            リソースバンドルが必要なロケール
     * @return リソースバンドル名配列
     */
    protected static String[] calcurateBundleNames(String baseName,
            Locale locale) {
        int length = 1;
        boolean l = locale.getLanguage().length() > 0;
        if (l) {
            length++;
        }
        boolean c = locale.getCountry().length() > 0;
        if (c) {
            length++;
        }
        boolean v = locale.getVariant().length() > 0;
        if (v) {
            length++;
        }
        String[] result = new String[length];
        int index = 0;
        result[index++] = baseName;

        if (!(l || c || v)) {
            return result;
        }

        StringBuffer buffer = new StringBuffer(baseName);
        buffer.append('_');
        buffer.append(locale.getLanguage());
        if (l) {
            result[index++] = new String(buffer);
        }

        if (!(c || v)) {
            return result;
        }
        buffer.append('_');
        buffer.append(locale.getCountry());
        if (c) {
            result[index++] = new String(buffer);
        }

        if (!v) {
            return result;
        }
        buffer.append('_');
        buffer.append(locale.getVariant());
        result[index++] = new String(buffer);

        return result;
    }

    /**
     * キャッシュしている内容をクリアします。
     */
    public static void clear() {
        cache.clear();
    }
}
