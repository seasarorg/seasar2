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
package org.seasar.extension.jdbc.dialect;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.FromClause;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.dialect.StandardDialect;

/**
 * @author higa
 * 
 */
public class StandardDialectTest extends TestCase {

	/**
	 * 
	 */
	public void testSetupJoin() {
		StandardDialect dialect = new StandardDialect();
		FromClause fromClause = new FromClause();
		fromClause.addSql("AAA", "_T1");
		List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();
		joinColumnMetaList.add(new JoinColumnMeta("BBB_ID", "BBB_ID"));
		dialect.setupJoin(fromClause, null, JoinType.LEFT_OUTER, "BBB", "_T2",
				"_T1", "_T2", joinColumnMetaList);
		assertEquals(
				" from AAA _T1 left outer join BBB _T2 on _T1.BBB_ID = _T2.BBB_ID",
				fromClause.toSql());
	}

}
