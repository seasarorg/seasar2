/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.impl;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.impl.sub.MogeBean;
import org.seasar.framework.beans.impl.sub.MogeBeanFactory;

/**
 * @author higa
 * 
 */
public class PropertyDescImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testSetValue() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("fff");
        propDesc.setValue(myBean, new BigDecimal(2));
        assertEquals(2, myBean.getFff());
    }

    /**
     * @throws Exception
     */
    public void testSetValue_null() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("fff");
        propDesc.setValue(myBean, null);
        assertEquals(0, myBean.getFff());
    }

    /**
     * @throws Exception
     */
    public void testSetValue_notWritable() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        try {
            propDesc.setValue(myBean, null);
            fail();
        } catch (IllegalPropertyRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetValue_notWritableWithField() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("jjj");
        try {
            propDesc.setValue(myBean, null);
            fail();
        } catch (IllegalPropertyRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetValue_invalidType() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("url");
        try {
        propDesc.setValue(myBean, new Object());
        fail();
        } catch (IllegalPropertyRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    public void testGetValue_notReable() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("iii");
        try {
            propDesc.getValue(myBean);
            fail();
        } catch (IllegalPropertyRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testGetValue_notReableWithField() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("kkk");
        try {
            propDesc.getValue(myBean);
            fail();
        } catch (IllegalPropertyRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetIllegalValue() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("fff");
        try {
            propDesc.setValue(myBean, "hoge");
            fail("1");
        } catch (IllegalPropertyRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testSetBigDecimalValue() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("ggg");
        propDesc.setValue(myBean, new Integer(1));
        assertEquals("1", new BigDecimal(1), myBean.getGgg());
    }

    /**
     * @throws Exception
     */
    public void testSetTimestampValue() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("hhh");
        propDesc.setValue(myBean, "2000/11/8");
        assertNotNull("1", myBean.getHhh());
    }

    /**
     * @throws Exception
     */
    public void testSetCalendarValue() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("cal");
        Date date = new Date();
        propDesc.setValue(myBean, date);
        assertEquals(date, myBean.getCal().getTime());
    }

    /**
     * @throws Exception
     */
    public void testSetIntegerValueToString() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("str");
        propDesc.setValue(myBean, new Integer(1));
        assertEquals("1", myBean.str);
    }

    /**
     * @throws Exception
     */
    public void testSetNullToString() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("str");
        propDesc.setValue(myBean, null);
        assertNull(myBean.str);
    }

    /**
     * @throws Exception
     */
    public void testConvertWithStringConstructor() throws Exception {
        MyBean myBean = new MyBean();
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("URL");
        propDesc.setValue(myBean, "http://www.seasar.org");
        assertNotNull("1", myBean.getURL());
    }

    /**
     * @throws Exception
     */
    public void testGetBeanDesc() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("URL");
        assertNotNull(propDesc.getBeanDesc());
    }

    /**
     * @throws Exception
     */
    public void testPackagePrivateBean() throws Exception {
        MogeBean moge = MogeBeanFactory.create("moge");
        BeanDesc beanDesc = new BeanDescImpl(moge.getClass());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("name");
        assertNotNull(propDesc);
        assertEquals("moge", propDesc.getValue(moge));
    }

    /**
     * 
     */
    public static class MyBean {

        private int fff_;

        private BigDecimal ggg_;

        private Timestamp hhh_;

        private String jjj;

        String kkk;

        private URL url_;

        private Calendar cal;

        /**
         * 
         */
        public String str;

        /**
         * @return
         */
        public String getAaa() {
            return null;
        }

        /**
         * @param a
         * @return
         */
        public String getBbb(Object a) {
            return null;
        }

        /**
         * @return
         */
        public boolean isCCC() {
            return true;
        }

        /**
         * @return
         */
        public Object isDdd() {
            return null;
        }

        /**
         * @return
         */
        public String getEee() {
            return null;
        }

        /**
         * @param eee
         */
        public void setEee(String eee) {
        }

        /**
         * @return
         */
        public int getFff() {
            return fff_;
        }

        /**
         * @param fff
         */
        public void setFff(int fff) {
            fff_ = fff;
        }

        /**
         * @return
         */
        public String getJjj() {
            return jjj;
        }

        /**
         * @param kkk
         */
        public void setKkk(String kkk) {
            this.kkk = kkk;
        }

        /**
         * @param arg1
         * @param arg2
         * @return
         */
        public Number add(Number arg1, Number arg2) {
            return new Integer(3);
        }

        /**
         * @return
         */
        public BigDecimal getGgg() {
            return ggg_;
        }

        /**
         * @param ggg
         */
        public void setGgg(BigDecimal ggg) {
            this.ggg_ = ggg;
        }

        /**
         * @return
         */
        public Timestamp getHhh() {
            return hhh_;
        }

        /**
         * @param hhh
         */
        public void setHhh(Timestamp hhh) {
            this.hhh_ = hhh;
        }

        /**
         * @param iii
         */
        public void setIii(String iii) {
        }

        /**
         * @return
         */
        public URL getURL() {
            return url_;
        }

        /**
         * @param url
         */
        public void setURL(URL url) {
            url_ = url;
        }

        /**
         * @return Returns the cal.
         */
        public Calendar getCal() {
            return cal;
        }

        /**
         * @param cal
         *            The cal to set.
         */
        public void setCal(Calendar cal) {
            this.cal = cal;
        }
    }

}
