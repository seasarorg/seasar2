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
package org.seasar.extension.jdbc.mapper;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.entity.Ccc;
import org.seasar.extension.jdbc.mapper.OneToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;

/**
 * @author higa
 * 
 */
public class OneToOneEntityMapperImplTest extends TestCase {

	/**
	 * 
	 * @throws Exception
	 */
	public void testMap() throws Exception {
		Field field = Bbb.class.getDeclaredField("id");
		PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field, 0);
		Field field2 = Bbb.class.getDeclaredField("name");
		PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
		Field field3 = Aaa.class.getDeclaredField("bbb");
		Field field4 = Bbb.class.getDeclaredField("aaa");

		OneToOneEntityMapperImpl entityMapper = new OneToOneEntityMapperImpl(
				Bbb.class, new PropertyMapper[] { propertyMapper,
						propertyMapper2 }, new int[] { 0 }, field3, field4);
		MappingContext mappingContext = new MappingContext(10);
		Object[] values = new Object[] { 1, "RESEARCH" };
		Aaa aaa = new Aaa();
		entityMapper.map(aaa, values, mappingContext);
		Bbb bbb = aaa.bbb;
		assertNotNull(bbb);
		assertNotNull(bbb.aaa);
		assertSame(aaa, bbb.aaa);

		entityMapper.map(aaa, values, mappingContext);
		assertSame(bbb, aaa.bbb);

		Aaa aaa2 = new Aaa();
		Object[] values2 = new Object[] { 2, "RESEARCH2" };
		entityMapper.map(aaa2, values2, mappingContext);
		assertNotNull(aaa2.bbb);
		assertNotSame(bbb, aaa2.bbb);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMap_noInverse() throws Exception {
		Field field = Ccc.class.getDeclaredField("id");
		PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field, 0);
		Field field2 = Ccc.class.getDeclaredField("name");
		PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
		Field field3 = Bbb.class.getDeclaredField("ccc");

		OneToOneEntityMapperImpl entityMapper = new OneToOneEntityMapperImpl(
				Ccc.class, new PropertyMapper[] { propertyMapper,
						propertyMapper2 }, new int[] { 0 }, field3, null);
		MappingContext mappingContext = new MappingContext(10);
		Object[] values = new Object[] { 1, "RESEARCH" };
		Bbb bbb = new Bbb();
		entityMapper.map(bbb, values, mappingContext);
		Ccc ccc = bbb.ccc;
		assertNotNull(ccc);

		entityMapper.map(bbb, values, mappingContext);
		assertSame(ccc, bbb.ccc);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testMap_entityNull() throws Exception {
		Field field = Bbb.class.getDeclaredField("id");
		PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field, 0);
		Field field2 = Bbb.class.getDeclaredField("name");
		PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
		Field field3 = Aaa.class.getDeclaredField("bbb");
		Field field4 = Bbb.class.getDeclaredField("aaa");

		OneToOneEntityMapperImpl entityMapper = new OneToOneEntityMapperImpl(
				Bbb.class, new PropertyMapper[] { propertyMapper,
						propertyMapper2 }, new int[] { 0 }, field3, field4);
		MappingContext mappingContext = new MappingContext(10);
		Object[] values = new Object[] { null, null };
		Aaa aaa = new Aaa();
		entityMapper.map(aaa, values, mappingContext);
		assertNull(aaa.bbb);

		entityMapper.map(aaa, values, mappingContext);
		assertNull(aaa.bbb);
	}
}