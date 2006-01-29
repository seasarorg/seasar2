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
package org.seasar.framework.beans.impl;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.impl.BeanDescImpl;
import org.seasar.framework.util.MathUtil;

import junit.framework.TestCase;

/**
 * @author higa
 *
 */
public class PropertyDescImplTest extends TestCase {
	
	public void testSetValue() throws Exception {
		MyBean myBean = new MyBean();
		BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
		PropertyDesc propDesc = beanDesc.getPropertyDesc("fff");
		propDesc.setValue(myBean, new BigDecimal(2));
		assertEquals("1", 2, myBean.getFff());
	}
	
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
	
	public void testSetBigDecimalValue() throws Exception {
		MyBean myBean = new MyBean();
		BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
		PropertyDesc propDesc = beanDesc.getPropertyDesc("ggg");
		propDesc.setValue(myBean, new Integer(1));
		assertEquals("1", new BigDecimal(1), myBean.getGgg());
	}
	
	public void testSetTimestampValue() throws Exception {
		MyBean myBean = new MyBean();
		BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
		PropertyDesc propDesc = beanDesc.getPropertyDesc("hhh");
		propDesc.setValue(myBean, "2000/11/8");
		assertNotNull("1", myBean.getHhh());
	}
	
	public void testConvertWithStringConstructor() throws Exception {
		MyBean myBean = new MyBean();
		BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
		PropertyDesc propDesc = beanDesc.getPropertyDesc("URL");
		propDesc.setValue(myBean, "http://www.seasar.org");
		assertNotNull("1", myBean.getURL());
	}
	
	public static class MyBean {

		private int fff_;
		private BigDecimal ggg_;
		private Timestamp hhh_;
		private URL url_;
		
		public String getAaa() {
			return null;
		}

		public String getBbb(Object a) {
			return null;
		}

		public boolean isCCC() {
			return true;
		}

		public Object isDdd() {
			return null;
		}

		public String getEee() {
			return null;
		}

		public void setEee(String eee) {
		}
		
		public int getFff() {
			return fff_;
		}
		
		public void setFff(int fff) {
			fff_ = fff;
		}
		
		public Number add(Number arg1, Number arg2) {
			return MathUtil.add(arg1, arg2);
		}
		
		public BigDecimal getGgg() {
			return ggg_;
		}
		
		public void setGgg(BigDecimal ggg) {
			this.ggg_ = ggg;
		}
		
		public Timestamp getHhh() {
			return hhh_;
		}
		
		public void setHhh(Timestamp hhh) {
			this.hhh_ = hhh;
		}
		
		public URL getURL() {
			return url_;
		}
		
		public void setURL(URL url) {
			url_ = url;
		}
	}

}
