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
package org.seasar.framework.message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.PropertiesUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.URLUtil;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFacade {

    private File file;

    private long lastModified;

    private MessageResourceBundle bundle;

    private MessageResourceBundleFacade parent;

    public MessageResourceBundleFacade(URL url) {
        setup(url);
    }

    public synchronized MessageResourceBundle getBundle() {
        if (isModified()) {
            bundle = createBundle(file);
        }
        if (parent != null) {
            bundle.setParent(parent.getBundle());
        }
        return bundle;
    }

    public synchronized MessageResourceBundleFacade getParent() {
        return parent;
    }

    public synchronized void setParent(MessageResourceBundleFacade parent) {
        this.parent = parent;
    }

    protected boolean isModified() {
        if (file != null && file.lastModified() > lastModified) {
            return true;
        }
        return false;
    }

    protected void setup(URL url) {
        AssertionUtil.assertNotNull("url", url);
        file = ResourceUtil.getFile(url);
        if (file != null) {
            lastModified = file.lastModified();
            bundle = createBundle(file);
        } else {
            bundle = createBundle(url);
        }
        if (parent != null) {
            bundle.setParent(parent.getBundle());
        }
    }

    protected static MessageResourceBundle createBundle(File file) {
        return new MessageResourceBundle(createProperties(file));
    }

    protected static MessageResourceBundle createBundle(URL url) {
        return new MessageResourceBundle(createProperties(url));
    }

    protected static Properties createProperties(File file) {
        return createProperties(FileInputStreamUtil.create(file));
    }

    protected static Properties createProperties(URL url) {
        return createProperties(URLUtil.openStream(url));
    }

    protected static Properties createProperties(InputStream is) {
        AssertionUtil.assertNotNull("is", is);
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        try {
            Properties properties = new Properties();
            PropertiesUtil.load(properties, is);
            return properties;
        } finally {
            InputStreamUtil.close(is);
        }
    }
}