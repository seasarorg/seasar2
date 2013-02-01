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
package org.seasar.extension.jdbc;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.RelationshipType;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class PropertyMetaTest extends TestCase {

	/**
	 * 
	 */
	public void testIsRelationship() {
		PropertyMeta meta = new PropertyMeta();
		assertFalse(meta.isRelationship());
		meta.setRelationshipType(RelationshipType.ONE_TO_MANY);
		assertTrue(meta.isRelationship());
	}
}