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
package org.seasar.framework.jpa.impl;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

import static org.easymock.EasyMock.*;

/**
 * @author taedium
 * 
 */
public class PersistenceUnitConfigurationImplTest extends S2TigerTestCase {

    PersistenceUnitConfigurationImpl cfg = new PersistenceUnitConfigurationImpl();

    @EasyMock
    private PersistenceUnitManager unitManager;

    @EasyMock
    private ResourceHandler resourceHandler;

    @EasyMock
    private ClassHandler classHandler;

    @EasyMock
    private ResourceAutoDetector resourceDetector;

    @EasyMock
    private ClassAutoDetector classDetector;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 
     */
    public void recordAddMappingFile() {
        expect(unitManager.getAbstractPersistenceUnitName("hoge.xml"))
                .andReturn("persistenceUnit");
        resourceHandler.processResource("hoge.xml", null);
    }

    /**
     * @throws Exception
     */
    public void testAddMappingFile() throws Exception {
        cfg.setPersistenceUnitManager(unitManager);
        cfg.addMappingFile("hoge.xml");
        cfg.detectMappingFiles("persistenceUnit", resourceHandler);
    }

    /**
     * 
     */
    public void recordAddPersistenceClass() {
        expect(unitManager.getAbstractPersistenceUnitName(Hoge.class))
                .andReturn("persistenceUnit");
        classHandler.processClass("org.seasar.framework.jpa.impl",
                "PersistenceUnitConfigurationImplTest$Hoge");
    }

    /**
     * @throws Exception
     */
    public void testAddPersistenceClass() throws Exception {
        cfg.setPersistenceUnitManager(unitManager);
        cfg.addPersistenceClass(Hoge.class);
        cfg.detectPersistenceClasses("persistenceUnit", classHandler);
    }

    /**
     * 
     */
    public void recordAddMappingFileAutoDetector() {
        resourceDetector.detect(isA(ResourceHandler.class));
    }

    /**
     * @throws Exception
     */
    public void testAddMappingFileAutoDetector() throws Exception {
        cfg.addMappingFileAutoDetector(resourceDetector);
        cfg.detectMappingFiles("persistenceUnit", resourceHandler);
    }

    /**
     * 
     */
    public void recordAddPersistenceClassAutoDetector() {
        classDetector.detect(isA(ClassHandler.class));
    }

    /**
     * @throws Exception
     */
    public void testAddPersistenceClassAutoDetector() throws Exception {
        cfg.addPersistenceClassAutoDetector(classDetector);
        cfg.detectPersistenceClasses("persistenceUnit", classHandler);
    }

    /**
     * @author taedium
     */
    public static class Hoge {
    }

}
