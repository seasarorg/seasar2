/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.hotdeploy.creator;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.creator.DaoCreator;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 * 
 */
public class DaoHotdeployCreatorTest extends HotdeployCreatorTestCase {

    protected ComponentCreator newOndemandCreator(NamingConvention convention) {
        return new DaoCreator(convention);
    }

    /**
     * @throws Exception
     */
    public void testIsTargetByComponentName() throws Exception {
        String name = "fooDao";
        ComponentDef cd = getComponentDef(name);
        assertNotNull(cd);
        assertEquals(name, cd.getComponentName());
        assertTrue(getContainer().hasComponentDef("barDao"));

        assertFalse(getContainer().hasComponentDef("hogeDao"));
        assertFalse(getContainer().hasComponentDef("hogeHogeDao"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetByClass() throws Exception {
        String pkgName = ClassUtil.getPackageName(getClass());
        Class clazz = ClassUtil.forName(pkgName + ".dao.FooDao");
        assertTrue(getContainer().hasComponentDef(clazz));

        clazz = ClassUtil.forName(pkgName + ".hoge.HogeDao");
        assertFalse(getContainer().hasComponentDef(clazz));
        clazz = ClassUtil.forName(pkgName + "hoge.HogeHogeDao");
        assertFalse(getContainer().hasComponentDef(clazz));
    }
}