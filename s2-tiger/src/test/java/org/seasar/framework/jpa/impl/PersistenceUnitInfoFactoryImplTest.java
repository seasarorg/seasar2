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

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.seasar.framework.unit.S2TigerTestCase;
import org.seasar.framework.util.ResourceUtil;

/**
 * {@link PersistenceUnitInfoFactoryImpl}のテストクラスです。
 * 
 * @author koichik
 */
public class PersistenceUnitInfoFactoryImplTest extends S2TigerTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jdbc.dicon");
    }

    /**
     * {@link PersistenceUnitInfoFactoryImpl#createPersistenceUnitInfo(URL)}をテストします。
     * 
     * @throws Exception
     */
    public void testCreatePersistenceUnitInfo() throws Exception {
        PersistenceUnitInfoFactoryImpl factory = new PersistenceUnitInfoFactoryImpl();
        factory.container = getContainer();

        URL url = ResourceUtil
                .getResource("org/seasar/framework/jpa/impl/META-INF/persistence.xml");
        List<PersistenceUnitInfo> list = factory.createPersistenceUnitInfo(url);
        assertNotNull(list);
        assertEquals(2, list.size());

        PersistenceUnitInfo info = list.get(0);
        assertEquals("foo", info.getPersistenceUnitName());
        assertEquals(PersistenceUnitTransactionType.RESOURCE_LOCAL, info
                .getTransactionType());
        assertNotNull(info.getJtaDataSource());
        assertNotNull(info.getNonJtaDataSource());
        assertEquals("hoge", info.getPersistenceProviderClassName());
        List<String> mappingFileNames = info.getMappingFileNames();
        assertNotNull(mappingFileNames);
        assertEquals(1, mappingFileNames.size());
        assertEquals("orm.xml", mappingFileNames.get(0));
        List<URL> jarFileNames = info.getJarFileUrls();
        System.out.println(jarFileNames);
        assertNotNull(jarFileNames);
        assertEquals(2, jarFileNames.size());
        assertTrue(jarFileNames.get(0).toExternalForm().endsWith(
                "org/seasar/framework/jpa/impl/foo.jar"));
        assertTrue(jarFileNames.get(1).toExternalForm().endsWith(
                "org/seasar/framework/jpa/impl/bar.jar"));
        List<String> classNames = info.getManagedClassNames();
        assertNotNull(classNames);
        assertEquals(3, classNames.size());
        assertEquals("hoge.Foo", classNames.get(0));
        assertEquals("hoge.Bar", classNames.get(1));
        assertEquals("hoge.Baz", classNames.get(2));
        assertTrue(info.excludeUnlistedClasses());
        Properties props = info.getProperties();
        assertNotNull(props);
        assertEquals(3, props.size());
        assertEquals("xxx", props.getProperty("aaa"));
        assertEquals("yyy", props.getProperty("bbb"));
        assertEquals("zzz", props.getProperty("ccc"));
        assertTrue(info.getPersistenceUnitRootUrl().toExternalForm().endsWith(
                "org/seasar/framework/jpa/impl/"));

        info = list.get(1);
        assertEquals("bar", info.getPersistenceUnitName());
        assertEquals(PersistenceUnitTransactionType.JTA, info
                .getTransactionType());
        assertNotNull(info.getJtaDataSource());
        assertNotNull(info.getNonJtaDataSource());
        assertEquals("org.hibernate.ejb.HibernatePersistence", info
                .getPersistenceProviderClassName());
        mappingFileNames = info.getMappingFileNames();
        assertNotNull(mappingFileNames);
        assertTrue(mappingFileNames.isEmpty());
        jarFileNames = info.getJarFileUrls();
        assertNotNull(jarFileNames);
        assertTrue(jarFileNames.isEmpty());
        classNames = info.getManagedClassNames();
        assertNotNull(classNames);
        assertTrue(classNames.isEmpty());
        assertFalse(info.excludeUnlistedClasses());
        props = info.getProperties();
        assertNotNull(props);
        assertTrue(props.isEmpty());
        assertTrue(info.getPersistenceUnitRootUrl().toExternalForm().endsWith(
                "org/seasar/framework/jpa/impl/"));
    }

}
