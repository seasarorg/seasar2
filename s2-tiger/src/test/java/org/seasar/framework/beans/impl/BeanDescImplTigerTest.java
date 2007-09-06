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
package org.seasar.framework.beans.impl;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author koichik
 */
public class BeanDescImplTigerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(HogeImpl.class);
        PropertyDesc pd = bd.getPropertyDesc("replyTo");
        assertEquals(HogeImpl.class, pd.getWriteMethod().getReturnType());
    }

    /**
     */
    public interface Hoge {

        /**
         * @param replyTo
         * @return
         */
        Hoge setReplyTo(String replyTo);

    }

    /**
     */
    public static class HogeImpl implements Hoge {

        private String replyTo;

        /**
         * @return
         */
        public String getReplyTo() {
            return replyTo;
        }

        public HogeImpl setReplyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

    }
}
