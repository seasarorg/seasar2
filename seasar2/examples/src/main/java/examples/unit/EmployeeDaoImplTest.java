/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package examples.unit;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;

import examples.unit.Employee;
import examples.unit.EmployeeDao;

public class EmployeeDaoImplTest extends S2TestCase {

	private EmployeeDao dao_;

	public EmployeeDaoImplTest(String arg0) {
		super(arg0);
	}

	public void setUp() {
		include("examples/unit/EmployeeDao.dicon");
	}

	public void testGetEmployeeTx() throws Exception {
		readXlsWriteDb("getEmployeePrepare.xls");
		Employee emp = dao_.getEmployee(9900);
		DataSet expected = readXls("getEmployeeExpected.xls");
		assertEquals("1", expected, emp);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(EmployeeDaoImplTest.class);
	}
}