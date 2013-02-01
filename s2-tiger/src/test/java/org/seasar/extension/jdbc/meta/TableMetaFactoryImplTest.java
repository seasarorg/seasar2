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
package org.seasar.extension.jdbc.meta;

import javax.persistence.Table;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

/**
 * @author higa
 * 
 */
public class TableMetaFactoryImplTest extends TestCase {

	private TableMetaFactoryImpl factory;

	@Override
	protected void setUp() {
		factory = new TableMetaFactoryImpl();
		factory.setPersistenceConvention(new PersistenceConventionImpl());
	}

	/**
	 * @throws Exception
	 */
	public void testCreateTableMeta_noannotation() throws Exception {
		TableMeta tableMeta = factory.createTableMeta(MyEntity.class,
				new EntityMeta("MyEntity"));
		assertEquals("MY_ENTITY", tableMeta.getName());
		assertNull(tableMeta.getSchema());
	}

	/**
	 * @throws Exception
	 */
	public void testCreateTableMeta_annotation() throws Exception {
		TableMeta tableMeta = factory.createTableMeta(MyEntity2.class,
				new EntityMeta("MyEntity2"));
		assertEquals("aaa", tableMeta.getName());
		assertEquals("bbb", tableMeta.getSchema());
	}

	private static class MyEntity {
	}

	@Table(name = "aaa", schema = "bbb")
	private static class MyEntity2 {
	}
}