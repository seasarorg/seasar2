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

import java.util.Properties;

/**
 * {@link MessageResourceBundle}の実装クラスです。
 * 
 * @author shot
 */
public class MessageResourceBundleImpl implements MessageResourceBundle {

    private Properties prop;

    private MessageResourceBundle parent;

    /**
     * {@link MessageResourceBundleImpl}を作成します。
     * 
     * @param prop
     */
    public MessageResourceBundleImpl(Properties prop) {
        this.prop = prop;
    }

    /**
     * {@link MessageResourceBundleImpl}を作成します。
     * 
     * @param prop
     * @param parent
     */
    public MessageResourceBundleImpl(Properties prop,
            MessageResourceBundle parent) {
        this(prop);
        setParent(parent);
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }
        if (prop.containsKey(key)) {
            return prop.getProperty(key);
        }
        return (parent != null) ? parent.get(key) : null;
    }

    public MessageResourceBundle getParent() {
        return parent;
    }

    public void setParent(MessageResourceBundle parent) {
        this.parent = parent;
    }
}