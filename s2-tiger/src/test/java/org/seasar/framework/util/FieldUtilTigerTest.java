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
package org.seasar.framework.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class FieldUtilTigerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfCollectionFromFieldType() throws Exception {
        assertEquals(String.class, FieldUtil
                .getElementTypeOfCollectionFromFieldType(Baz.class
                        .getField("collectionOfString")));
        assertEquals(BigDecimal.class, FieldUtil
                .getElementTypeOfCollectionFromFieldType(Baz.class
                        .getField("collectionOfBigDecimal")));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfListFromFieldType() throws Exception {
        assertEquals(String.class, FieldUtil
                .getElementTypeOfListFromFieldType(Baz.class
                        .getField("listOfString")));
        assertEquals(BigDecimal.class, FieldUtil
                .getElementTypeOfListFromFieldType(Baz.class
                        .getField("listOfBigDecimal")));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfSetFromFieldType() throws Exception {
        assertEquals(String.class, FieldUtil
                .getElementTypeOfSetFromFieldType(Baz.class
                        .getField("setOfString")));
        assertEquals(BigDecimal.class, FieldUtil
                .getElementTypeOfSetFromFieldType(Baz.class
                        .getField("setOfBigDecimal")));
    }

    /**
     * 
     */
    public static class Baz {

        /** */
        public Collection<String> collectionOfString;

        /** */
        public Collection<BigDecimal> collectionOfBigDecimal;

        /** */
        public List<String> listOfString;

        /** */
        public List<BigDecimal> listOfBigDecimal;

        /** */
        public Set<String> setOfString;

        /** */
        public Set<BigDecimal> setOfBigDecimal;
    }

}
