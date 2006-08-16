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
package org.seasar.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author shot
 *
 */
public class ModifierUtil {

    private ModifierUtil() {
    }

    public static boolean isPublic(Method m) {
        return isPublic(m.getModifiers());
    }

    public static boolean isPublicStaticFinalField(Field f) {
        return isPublicStaticFinal(f.getModifiers());
    }

    public static boolean isPublicStaticFinal(int modifier) {
        return isPublic(modifier) && isStatic(modifier) && isFinal(modifier);
    }

    public static boolean isPublic(int modifier) {
        return Modifier.isPublic(modifier);
    }

    public static boolean isStatic(int modifier) {
        return Modifier.isStatic(modifier);
    }

    public static boolean isFinal(int modifier) {
        return Modifier.isFinal(modifier);
    }

}
