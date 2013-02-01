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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * @author taedium
 * 
 */
public class MappingFileAutoDetectorTest extends S2TestCase {

    private ResourceAutoDetector detector;

    @Override
    protected void setUp() throws Exception {
        include("MappingFileAutoDetectorTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testDetect() throws Exception {
        final List<String> paths = new ArrayList<String>();
        detector.detect(new ResourceHandler() {

            public void processResource(String path, InputStream is) {
                paths.add(path);
            }
        });
        assertEquals(6, paths.size());
        assertTrue(paths.contains("META-INF/fooOrm.xml"));
        assertTrue(paths
                .contains("org/seasar/framework/jpa/entity/hogeOrm.xml"));
        assertTrue(paths
                .contains("org/seasar/framework/jpa/entity/sub/barOrm.xml"));
        assertTrue(paths.contains("org/seasar/framework/jpa/dao/hogeOrm.xml"));
        assertTrue(paths
                .contains("org/seasar/framework/jpa/dao/sub/barOrm.xml"));
        assertTrue(paths.contains("org/junit/runner/RunWith.class"));
    }

}
