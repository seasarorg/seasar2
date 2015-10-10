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
package org.seasar.framework.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * 配列に対するユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class ArrayUtil {

    /**
     * インスタンスを構築します。
     */
    protected ArrayUtil() {
    }

    /**
     * 配列にオブジェクトを追加します。
     * 
     * @param array
     * @param obj
     * @return オブジェクトが追加された結果の配列
     */
    public static Object[] add(Object[] array, Object obj) {
        if (array == null) {
            throw new EmptyRuntimeException("array");
        }
        Object[] newArray = (Object[]) Array.newInstance(array.getClass()
                .getComponentType(), array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = obj;
        return newArray;
    }

    /**
     * intの配列にintを追加します。
     * 
     * @param array
     * @param value
     * @return オブジェクトが追加された結果の配列
     */
    public static int[] add(int[] array, int value) {
        if (array == null) {
            throw new EmptyRuntimeException("array");
        }
        int[] newArray = (int[]) Array.newInstance(int.class, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = value;
        return newArray;
    }

    /**
     * 配列に配列を追加します。
     * 
     * @param a
     * @param b
     * @return 配列が追加された結果の配列
     */
    public static Object[] add(final Object[] a, final Object[] b) {
        if (a != null && b != null) {
            if (a.length != 0 && b.length != 0) {
                Object[] array = (Object[]) Array.newInstance(a.getClass()
                        .getComponentType(), a.length + b.length);
                System.arraycopy(a, 0, array, 0, a.length);
                System.arraycopy(b, 0, array, a.length, b.length);
                return array;
            } else if (b.length == 0) {
                return a;
            } else {
                return b;
            }
        } else if (b == null) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * 配列中のオブジェクトのindexを返します。
     * 
     * @param array
     * @param obj
     * @return 配列中のオブジェクトのindex
     */
    public static int indexOf(Object[] array, Object obj) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                Object o = array[i];
                if (o != null) {
                    if (o.equals(obj)) {
                        return i;
                    }
                } else if (obj == null) {
                    return i;

                }
            }
        }
        return -1;
    }

    /**
     * 配列中のcharのindexを返します。
     * 
     * @param array
     * @param ch
     * @return 配列中のcharのindex
     */
    public static int indexOf(char[] array, char ch) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                char c = array[i];
                if (ch == c) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 配列中から対象のオブジェクトを削除します。
     * 
     * @param array
     * @param obj
     * @return 削除後の配列
     */
    public static Object[] remove(Object[] array, Object obj) {
        int index = indexOf(array, obj);
        if (index < 0) {
            return array;
        }
        Object[] newArray = (Object[]) Array.newInstance(array.getClass()
                .getComponentType(), array.length - 1);
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, newArray, index, newArray.length
                    - index);
        }
        return newArray;
    }

    /**
     * 配列が空かどうかを返します。
     * 
     * @param arrays
     * @return 配列が空かどうか
     */
    public static boolean isEmpty(Object[] arrays) {
        return (arrays == null || arrays.length == 0);
    }

    /**
     * 配列にオブジェクトが含まれているかどうかを返します。
     * 
     * @param array
     * @param obj
     * @return 配列にオブジェクトが含まれているかどうか
     */
    public static boolean contains(Object[] array, Object obj) {
        return -1 < indexOf(array, obj);
    }

    /**
     * 配列にcharが含まれているかどうかを返します。
     * 
     * @param array
     * @param ch
     * @return 配列にcharが含まれているかどうか
     */
    public static boolean contains(char[] array, char ch) {
        return -1 < indexOf(array, ch);
    }

    /**
     * 順番は無視して2つの配列が等しいかどうかを返します。
     * 
     * @param array1
     * @param array2
     * @return 順番は無視して2つの配列が等しいかどうか
     */
    public static boolean equalsIgnoreSequence(Object[] array1, Object[] array2) {
        if (array1 == null && array2 == null) {
            return true;
        } else if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        List list = Arrays.asList(array2);
        for (int i = 0; i < array1.length; i++) {
            Object o1 = array1[i];
            if (!list.contains(o1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 配列を文字列に変換します。
     * 
     * @param array
     * @return 配列の文字列表現
     */
    public static String toString(Object[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                sb.append('[');
            } else {
                sb.append(", ");
            }
            sb.append(String.valueOf(array[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * <code>primitive</code>型を考慮して配列に値を設定します。
     * 
     * @param array
     * @param valueType
     * @param value
     * @param index
     */
    public static void setArrayValue(Object array, Class valueType,
            Object value, int index) {
        if (value == null) {
            return;
        }
        if (valueType == int.class) {
            Array.setInt(array, index, IntegerConversionUtil
                    .toPrimitiveInt(value));
        } else if (valueType == double.class) {
            Array.setDouble(array, index, DoubleConversionUtil
                    .toPrimitiveDouble(value));
        } else if (valueType == long.class) {
            Array.setLong(array, index, LongConversionUtil
                    .toPrimitiveLong(value));
        } else if (valueType == float.class) {
            Array.setFloat(array, index, FloatConversionUtil
                    .toPrimitiveFloat(value));
        } else if (valueType == short.class) {
            Array.setShort(array, index, ShortConversionUtil
                    .toPrimitiveShort(value));
        } else if (valueType == boolean.class) {
            Array.setBoolean(array, index, BooleanConversionUtil
                    .toPrimitiveBoolean(value));
        } else if (valueType == char.class) {
            Array.setChar(array, index, ((Character) value).charValue());
        }
        Array.set(array, index, value);
    }

    /**
     * 配列をオブジェクトの配列に変換します。
     * 
     * @param obj
     * @return オブジェクトの配列
     */
    public static Object[] toObjectArray(Object obj) {
        int length = Array.getLength(obj);
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = Array.get(obj, i);
        }
        return array;
    }

    /**
     * 配列をリストに変換します。
     * 
     * @param obj
     *            配列
     * @return リスト
     */
    public static List toList(Object obj) {
        int length = Array.getLength(obj);
        List list = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            list.add(Array.get(obj, i));
        }
        return list;
    }
}
