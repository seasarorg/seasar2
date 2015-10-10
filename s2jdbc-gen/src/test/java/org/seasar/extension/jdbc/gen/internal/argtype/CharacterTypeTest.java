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
package org.seasar.extension.jdbc.gen.internal.argtype;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class CharacterTypeTest {

    private CharacterType characterType = new CharacterType();

    /**
     * 
     */
    @Test
    public void testToObject() {
        assertEquals(new Character('\u0000'), characterType.toObject("\\u0000"));
        assertEquals(new Character('a'), characterType.toObject("\\u0097"));
    }

    /**
     * 
     */
    @Test
    public void testToText() {
        assertEquals("\\u0000", characterType.toText(new Character('\u0000')));
        assertEquals("\\u0097", characterType.toText(new Character('a')));
    }

}
