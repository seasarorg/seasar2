/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.customizer;

import org.seasar.framework.aop.annotation.DependencyLookup;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class DependencyLookupCustomizerTest extends S2FrameworkTestCase {

    DependencyLookupCustomizer customizer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("std-customizer-tiger.dicon");
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        ComponentDefImpl cd = new ComponentDefImpl(Hot.class);
        customizer.customize(cd);
        assertEquals(1, cd.getAspectDefSize());
    }

    /** */
    public static class Hot {

        /**
         * @return
         */
        @DependencyLookup
        public Pepper getPepper() {
            return null;
        }

        /**
         * @return
         */
        public Pippupe getPippupe() {
            return null;
        }

        /**
         * @return
         */
        @DependencyLookup
        protected Pappi getPappi() {
            return null;
        }
    }

    /** */
    public interface Pepper {
    }

    /** */
    public interface Pippupe {
    }

    /** */
    public interface Pappi {
    }

}
