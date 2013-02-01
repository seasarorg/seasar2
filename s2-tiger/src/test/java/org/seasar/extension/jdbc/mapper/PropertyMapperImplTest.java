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

import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;

/**
 * @author higa
 * 
 */
public class PropertyMapperImplTest extends TestCase {

	/**
	 * @throws Exception
	 */
	public void testMap() throws Exception {
		Field field = Aaa.class.getDeclaredField("id");
		PropertyMapperImpl mapper = new PropertyMapperImpl(field, 0);
		Aaa aaa = new Aaa();
		mapper.map(aaa, new Object[] { new Integer(1) });
		assertEquals(new Integer(1), aaa.id);
	}
}