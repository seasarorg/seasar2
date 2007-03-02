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
package test.examples.jsf.dao;

import java.io.File;
import java.io.InputStream;

import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author manhole
 */
class MyMockServletContextImpl extends MockServletContextImpl {
    private static final long serialVersionUID = 1L;

    public MyMockServletContextImpl() {
        this("/s2jsf");
    }

    public MyMockServletContextImpl(String path) {
        super(path);
    }

    public InputStream getResourceAsStream(String path) {
        if (path.startsWith("/WEB-INF")) {
            File root = getTopDirectoryContainsFor("pom.xml");
            File f = new File(root, "/src/main/webapp" + path);
            return FileInputStreamUtil.create(f);
        } else {
            return super.getResourceAsStream(path);
        }
    }

    private File getTopDirectoryContainsFor(String fileName) {
        File found = null;
        for (File f = ResourceUtil.getResourceAsFile("."); f != null; f = f
                .getParentFile()) {
            if (new File(f, fileName).exists()) {
                found = f;
            } else if (found != null) {
                return found;
            }
        }
        return found;
    }
}