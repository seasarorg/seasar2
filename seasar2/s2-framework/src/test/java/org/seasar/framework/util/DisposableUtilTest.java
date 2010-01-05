/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class DisposableUtilTest extends TestCase {

    private int count;

    protected void setUp() throws Exception {
        DisposableUtil.dispose();
    }

    /**
     * @throws Exception
     */
    public void test1() throws Exception {
        DisposableUtil.add(new TestDisposable());
        assertEquals(1, DisposableUtil.disposables.size());
        DisposableUtil.dispose();
        assertEquals(1, count);
        assertEquals(0, DisposableUtil.disposables.size());
    }

    /**
     * @throws Exception
     */
    public void test2() throws Exception {
        DisposableUtil.add(new TestDisposable());
        DisposableUtil.add(new TestDisposable());
        assertEquals(2, DisposableUtil.disposables.size());
        DisposableUtil.dispose();
        assertEquals(2, count);
        assertEquals(0, DisposableUtil.disposables.size());
    }

    /**
     * @throws Exception
     */
    public void test3() throws Exception {
        DisposableUtil.add(new TestDisposable());
        DisposableUtil.add(new TestDisposable2());
        DisposableUtil.add(new TestDisposable());
        assertEquals(3, DisposableUtil.disposables.size());
        DisposableUtil.dispose();
        assertEquals(3, count);
        assertEquals(0, DisposableUtil.disposables.size());
    }

    /**
     *
     */
    public class TestDisposable implements Disposable {
        public void dispose() {
            ++count;
        }
    }

    /**
     *
     */
    public class TestDisposable2 implements Disposable {
        public void dispose() {
            ++count;
            throw new RuntimeException();
        }
    }

    /**
     * @throws Exception
     */
    public void deregisterAllDriversTest() throws Exception {
        Class clazz = Class.forName("org.hsqldb.jdbcDriver");
        Driver d = (Driver) clazz.newInstance();
        DriverManager.registerDriver(d);
        int i = 0;
        for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements(); i++) {
            e.nextElement();
        }
        assertEquals(2, i);
        DisposableUtil.deregisterAllDrivers();
        int j = 0;
        for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements(); j++) {
            e.nextElement();
        }
        assertEquals(0, j);
    }
}
