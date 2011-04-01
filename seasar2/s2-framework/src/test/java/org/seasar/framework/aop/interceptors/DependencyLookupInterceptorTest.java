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
package org.seasar.framework.aop.interceptors;

import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class DependencyLookupInterceptorTest extends S2FrameworkTestCase {

    Hot hot;

    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        assertNotNull(hot.getPippupe()); // by Name
        assertNotNull(hot.getPappi()); // by Type
        Pepper[] hotPepper = hot.getHotPepper(); // findAll
        assertNotNull(hotPepper);
        assertEquals(2, hotPepper.length);
        try {
            hot.getPepper(); // too many
            fail();
        } catch (TooManyRegistrationRuntimeException expected) {
        }
    }

    /** */
    public static class Hot {
        /**
         * @return
         */
        public Pepper getPippupe() {
            return null;
        }

        /**
         * @return
         */
        public Pappi getPappi() {
            return null;
        }

        /**
         * @return
         */
        public Pepper[] getHotPepper() {
            return null;
        }

        /**
         * @return
         */
        public Pepper getPepper() {
            return null;
        }
    }

    /** */
    public interface Pepper {
    }

    /** */
    public static class Pippupe implements Pepper {
    }

    /** */
    public interface Pappi extends Pepper {
    }

    /** */
    public static class Poppi implements Pappi {
    }

}
