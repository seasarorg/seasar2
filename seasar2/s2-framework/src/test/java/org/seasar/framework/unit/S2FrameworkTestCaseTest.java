/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;

import junitx.framework.ArrayAssert;

import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.warmdeploy.WarmdeployBehavior;
import org.seasar.framework.env.Env;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;

/**
 * @author higa
 * 
 */
public class S2FrameworkTestCaseTest extends S2FrameworkTestCase {

    private static final String J2EE_PATH = "S2FrameworkTestCaseTest_j2ee.dicon";

    private boolean testAaaSetUpInvoked_ = false;

    private Date date_;

    private String ccc_;

    private HashMap bbb_;

    private Date ddd_;

    private List list1;

    /**
     * 
     */
    public S2FrameworkTestCaseTest() {
    }

    /**
     * @param name
     */
    public S2FrameworkTestCaseTest(String name) {
        super(name);
        if (name.equals("testNotWarmdeploy")) {
            setWarmDeploy(false);
        }
    }

    /**
     * 
     */
    public void setUpAaa() {
        testAaaSetUpInvoked_ = true;
    }

    /**
     * 
     */
    public void testAaa() {
        assertEquals("1", true, testAaaSetUpInvoked_);
    }

    /**
     * 
     */
    public void tearDownAaa() {
        System.out.println("tearDownAaa");
    }

    /**
     * 
     */
    public void setUpBbbTx() {
        include(J2EE_PATH);
    }

    /**
     * 
     */
    public void testBbbTx() {
        System.out.println("testBbbTx");
    }

    /**
     * 
     */
    public void setUpBindField() {
        include(J2EE_PATH);
        register(Date.class);
    }

    /**
     * 
     */
    public void testBindField() {
        assertNotNull("1", date_);
    }

    /**
     * 
     */
    public void tearDownBindField() {
        assertNull(date_);
    }

    /**
     * 
     */
    public void setUpBindField2() {
        include("bbb.dicon");
    }

    /**
     * 
     */
    public void testBindField2() {
        assertNotNull("1", bbb_);
        assertNotNull("2", ddd_);
    }

    /**
     * 
     */
    public void tearDownBindField2() {
        assertNull(bbb_);
        assertNull(ddd_);
    }

    /**
     * 
     */
    public void setUpBindField3() {
        include("ccc.dicon");
    }

    /**
     * 
     */
    public void testBindField3() {
        assertNotNull("1", list1);
    }

    /**
     * 
     */
    public void tearDownBindField3() {
        assertNull(list1);
    }

    /**
     * 
     */
    public void testInclude() {
        include("aaa.dicon");
        try {
            getComponent(Date.class);
            fail("1");
        } catch (TooManyRegistrationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * 
     */
    public void testConvertPath() {
        assertEquals("org/seasar/framework/unit/aaa.dicon",
                convertPath("aaa.dicon"));
        assertEquals("j2ee.dicon", convertPath("j2ee.dicon"));
        assertEquals("notfound.dicon", convertPath("notfound.dicon"));
    }

    /**
     * 
     */
    public void setUpIsAssignableFrom() {
        include("bbb.dicon");
    }

    /**
     * 
     */
    public void testIsAssignableFrom() {
        assertEquals("1", "hoge", ccc_);
    }

    /**
     * 
     */
    public void testEmptyComponent() {
        include("empty.dicon");
    }

    /**
     * @throws Exception
     */
    public void testEnv() throws Exception {
        assertEquals("env_ut.txt", Env.getFilePath());
        assertEquals("ut", Env.getValue());
    }

    /**
     * @throws Exception
     */
    public void setUpWarmdeploy() throws Exception {
        include("aop.dicon");
    }

    /**
     * @throws Exception
     */
    public void testWarmdeploy() throws Exception {
        assertTrue(S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
        assertNotNull(getComponent("fooDao"));
    }

    /**
     * @throws Exception
     */
    public void testNotWarmdeploy() throws Exception {
        assertFalse(S2ContainerBehavior.getProvider() instanceof WarmdeployBehavior);
    }

    /**
     * @throws Exception
     */
    public void testGetResponseString() throws Exception {
        final MockHttpServletResponse res = getResponse();
        final PrintWriter writer = res.getWriter();
        writer.write("ZXCV");
        writer.write("abc");

        assertEquals("ZXCVabc", res.getResponseString());
    }

    /**
     * @throws Exception
     */
    public void testGetResponseBytes() throws Exception {
        final MockHttpServletResponse res = getResponse();
        final ServletOutputStream os = res.getOutputStream();
        os.write(new byte[] { 3, 2, 1 });
        os.write(new byte[] { 9, 8, 7, 6 });

        final byte[] bytes = res.getResponseBytes();
        assertEquals(7, bytes.length);
        ArrayAssert.assertEquals(new byte[] { 3, 2, 1, 9, 8, 7, 6 }, bytes);
    }

    /**
     * @return
     */
    public String getRootDiconGetRootDicon() {
        return "aop.dicon";
    }

    /**
     * @throws Throwable
     */
    public void testGetRootDicon() throws Throwable {
        assertEquals("aop.dicon", resolveRootDicon());
        assertEquals("aop.dicon", getContainer().getPath());
    }

    /**
     * 
     */
    public static class Hoge {

        private String aaa;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }
    }
}
