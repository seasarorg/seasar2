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
package examples.s2junit4;

import static org.seasar.framework.unit.S2Assert.assertEquals;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;

@RunWith(Seasar2.class)
public class EmployeeDaoImplTest {

    private TestContext ctx;

    private EmployeeDao dao;

    public void getEmployee() throws Exception {
        Employee emp = dao.getEmployee(9900);
        assertEquals("1", ctx.getExpected(), emp);
    }

    @Ignore("not implemented.")
    public void getEmployeeByName() throws Exception {
    }

}