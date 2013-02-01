/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.autodetector;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.jpa.entity.Hoge;
import org.seasar.framework.jpa.entity.sub.Foo;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class PersistenceClassAutoDetectorTest extends S2TestCase {

    private ClassAutoDetector detector;

    @Override
    protected void setUp() throws Exception {
        include("PersistenceClassAutoDetectorTest.dicon");
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testDetect() throws Exception {
        final List<Class> classes = new ArrayList<Class>();
        detector.detect(new ClassTraversal.ClassHandler() {

            public void processClass(String packageName, String shortClassName) {
                String name = ClassUtil.concatName(packageName, shortClassName);
                Class clazz = ReflectionUtil.forNameNoException(name);
                classes.add(clazz);
            }
        });
        assertTrue(classes.size() == 3);
        assertTrue(classes.contains(Hoge.class));
        assertTrue(classes.contains(Foo.class));
        assertTrue(classes.contains(RunWith.class));
    }
}
