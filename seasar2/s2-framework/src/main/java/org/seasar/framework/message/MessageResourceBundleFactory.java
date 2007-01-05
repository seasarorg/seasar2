/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import org.seasar.framework.util.ResourceUtil;

/**
 * @author shot
 */
public class MessageResourceBundleFactory {

    private static final String PROPERTIES_EXT = ".properties";

    private static Map cache = new HashMap();

    public static MessageResourceBundle getBundle(String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    public static MessageResourceBundle getBundle(String baseName, Locale locale) {
        MessageResourceBundle bundle = getNullableBundle(baseName, locale);
        if (bundle != null) {
            return bundle;
        }
        throw new ResourceNotFoundRuntimeException(baseName);
    }

    public static MessageResourceBundle getNullableBundle(String baseName) {
        return getNullableBundle(baseName, Locale.getDefault());
    }

    public static MessageResourceBundle getNullableBundle(String baseName,
            Locale locale) {
        AssertionUtil.assertNotNull("baseName", baseName);
        AssertionUtil.assertNotNull("locale", locale);

        String base = baseName.replace('.', '/');
        String path = base + "_" + locale.getLanguage() + PROPERTIES_EXT;
        MessageResourceBundleFacade facade = loadFacade(path);
        String parentPath = base + PROPERTIES_EXT;
        MessageResourceBundleFacade parentFacade = loadFacade(parentPath);
        if (facade != null && parentFacade != null) {
            facade.setParent(parentFacade);
            return facade.getBundle();
        } else if (facade != null) {
            return facade.getBundle();
        } else if (parentFacade != null) {
            return parentFacade.getBundle();
        }
        return null;
    }

    protected static MessageResourceBundleFacade loadFacade(String path) {
        synchronized (cache) {
            MessageResourceBundleFacade facade = (MessageResourceBundleFacade) cache
                    .get(path);
            if (facade != null) {
                return facade;
            }
            URL url = ResourceUtil.getResourceNoException(path);
            if (url != null) {
                facade = new MessageResourceBundleFacade(url);
                cache.put(path, facade);
                return facade;
            }
        }
        return null;
    }

    public static void clear() {
        cache.clear();
    }

}