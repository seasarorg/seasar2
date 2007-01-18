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
package org.seasar.extension.persistence.factory;

import junit.framework.TestCase;

import org.seasar.extension.persistence.TableMeta;
import org.seasar.extension.persistence.annotation.Table;
import org.seasar.extension.persistence.entity.Employee;
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

	public void testCreateTableMeta() throws Exception {
		TableMeta tableMeta = factory.createTableMeta(Employee.class,
				"Employee");
		assertEquals("EMPLOYEE", tableMeta.getName());
	}

	public void testCreateTableMeta_annotable() throws Exception {
		TableMeta tableMeta = factory.createTableMeta(Hoge.class, "Hoge");
		assertEquals("aaa", tableMeta.getName());
	}

	@Table(name = "aaa")
	private static class Hoge {

	}
}