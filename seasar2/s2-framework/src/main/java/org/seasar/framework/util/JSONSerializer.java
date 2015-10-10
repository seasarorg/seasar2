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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * JSON形式の文字列を解析します。 ObjectをJSON文字列へシリアライズ、JSON文字列をObjectへ評価します。
 * 
 * @author mopemope
 * @deprecated このクラスの代わりに<a
 *             href="http://jsonic.sourceforge.jp/">JSONIC</a>等を使用してください
 */
public class JSONSerializer {
    private static final String COMMA = ",";

    private static final String COLON = ":";

    private static final String SINGLE_QUOTE = "'";

    private static final String DOUBLE_QUOTE = "\"";

    private static final String NULL_STRING = "null";

    private static final String TRUE_STRING = "true";

    private static final String FALSE_STRING = "false";

    private static final String START_BRACKET = "[";

    private static final String END_BRACKET = "]";

    private static final String START_BRACE = "{";

    private static final String END_BRACE = "}";

    /**
     * ObjectをJSONにシリアライズします。
     * 
     * @param o
     *            シリアライズ対象{@link java.lang.Object}
     * @return JSON形式の文字列
     */
    public static String serialize(Object o) {
        StringBuffer buf = new StringBuffer(100);
        appendSerializeObject(buf, o);
        return buf.toString();
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param o
     *            シリアライズ対象{@link java.lang.Object}
     */
    public static void appendSerializeObject(StringBuffer buf, Object o) {
        if (o == null) {
            buf.append(JSONSerializer.NULL_STRING);
        } else if (o instanceof String) {
            buf.append(quote((String) o));
        } else if (o instanceof Float) {
            appendSerializeFloat(buf, (Float) o);
        } else if (o instanceof Double) {
            appendSerializeDouble(buf, (Double) o);
        } else if (o instanceof Number) {
            buf.append(o.toString());
        } else if (o instanceof Boolean) {
            appendSerializeBoolean(buf, (Boolean) o);
        } else if (o instanceof Collection) {
            appendSerializeArray(buf, ((Collection) o).toArray());
        } else if (o instanceof Object[]) {
            appendSerializeArray(buf, (Object[]) o);
        } else if (o instanceof Map) {
            appendSerializeMap(buf, (Map) o);
        } else if (o.getClass().isArray()) {
            appendSerializeObjectArray(buf, o);
        } else {
            appendSerializeBean(buf, o);
        }
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param f
     *            シリアライズ対象{@link java.lang.Float}
     */
    public static void appendSerializeFloat(StringBuffer buf, Float f) {
        if (f.isNaN() || f.isInfinite()) {
            throw new IllegalArgumentException(f.toString());
        }
        buf.append(f.toString());
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param d
     *            シリアライズ対象{@link java.lang.Double}
     */
    public static void appendSerializeDouble(StringBuffer buf, Double d) {
        if (d.isNaN() || d.isInfinite()) {
            throw new IllegalArgumentException(d.toString());
        }
        buf.append(d.toString());
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param b
     *            シリアライズ対象{@link java.lang.Boolean}
     */
    public static void appendSerializeBoolean(StringBuffer buf, Boolean b) {
        if (Boolean.TRUE.equals(b)) {
            buf.append(JSONSerializer.TRUE_STRING);
        } else {
            buf.append(JSONSerializer.FALSE_STRING);
        }
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param array
     *            シリアライズ対象{@link java.lang.Object}配列
     */
    public static void appendSerializeArray(StringBuffer buf, Object[] array) {
        int length = array.length;
        buf.append(JSONSerializer.START_BRACKET);
        for (int i = 0; i < length; ++i) {
            appendSerializeObject(buf, array[i]);
            buf.append(JSONSerializer.COMMA);
        }
        if (length > 0) {
            buf.setLength(buf.length() - 1);
        }
        buf.append(JSONSerializer.END_BRACKET);
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param map
     *            シリアライズ対象{@link java.util.Map}
     */
    public static void appendSerializeMap(StringBuffer buf, Map map) {
        buf.append(JSONSerializer.START_BRACE);
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            buf.append(quote(key) + JSONSerializer.COLON);
            appendSerializeObject(buf, map.get(key));
            buf.append(JSONSerializer.COMMA);
        }
        if (map.size() > 0) {
            buf.setLength(buf.length() - 1);
        }
        buf.append(JSONSerializer.END_BRACE);
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param bean
     *            シリアライズ対象{@link java.lang.Object}
     */
    public static void appendSerializeBean(StringBuffer buf, Object bean) {
        buf.append(JSONSerializer.START_BRACE);
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            buf.append(pd.getPropertyName() + JSONSerializer.COLON);
            appendSerializeObject(buf, pd.getValue(bean));
            buf.append(JSONSerializer.COMMA);
        }
        if (beanDesc.getPropertyDescSize() > 0) {
            buf.setLength(buf.length() - 1);
        }
        buf.append(JSONSerializer.END_BRACE);
    }

    /**
     * 指定したバッファにJSONシリアライズした結果を追加します。
     * 
     * @param buf
     *            {@link java.lang.StringBuffer}
     * @param o
     *            シリアライズ対象{@link java.lang.Object}
     */
    public static void appendSerializeObjectArray(StringBuffer buf, Object o) {
        int length = Array.getLength(o);
        buf.append(JSONSerializer.START_BRACKET);
        for (int i = 0; i < length; i++) {
            Object value = Array.get(o, i);
            appendSerializeObject(buf, value);
            buf.append(JSONSerializer.COMMA);
        }
        if (length > 0) {
            buf.setLength(buf.length() - 1);
        }
        buf.append(JSONSerializer.END_BRACKET);
    }

    /**
     * 文字列を引用付で囲みます。
     * 
     * @param str
     *            対象の文字列
     * @return 引用付で囲まれた{@link java.lang.String}
     */
    public static String quote(String str) {
        if (str == null || str.length() == 0) {
            return "\"\"";
        }
        char current = 0;
        int len = str.length();
        StringBuffer sb = new StringBuffer(len + 4);
        sb.append('"');
        for (int i = 0; i < len; ++i) {
            current = str.charAt(i);
            switch (current) {
            case '\\':
            case '/':
            case '"':
                sb.append('\\');
                sb.append(current);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (current < ' ') {
                    String t = "000" + Integer.toHexString(current);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(current);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * 指定した文字列がJSONのObject形式か判断します。
     * 
     * @param str
     *            対象の文字列
     * @return JSONのObject形式であればtrue、そうでなければfalse
     */
    public static boolean isObject(String str) {
        return str.startsWith(JSONSerializer.START_BRACE)
                && str.endsWith(JSONSerializer.END_BRACE);
    }

    /**
     * 指定した文字列がJSONのArray形式か判断します。
     * 
     * @param str
     *            対象の文字列
     * @return JSONのArray形式であればtrue、そうでなければfalse
     */
    public static boolean isArray(String str) {
        return str.startsWith(JSONSerializer.START_BRACKET)
                && str.endsWith(JSONSerializer.END_BRACKET);
    }

    /**
     * 指定した文字列がJSONのString形式か判断します。
     * 
     * @param str
     *            対象の文字列
     * @return JSONのString形式であればtrue、そうでなければfalse
     */
    public static boolean isString(String str) {
        return (str.startsWith(JSONSerializer.SINGLE_QUOTE)
                && str.endsWith(JSONSerializer.SINGLE_QUOTE) || str
                .startsWith(JSONSerializer.DOUBLE_QUOTE)
                && str.endsWith(JSONSerializer.DOUBLE_QUOTE));
    }

    /**
     * 指定したJSON文字列を評価します。
     * 
     * @param str
     *            対象の文字列
     * @return 評価された{@link javac.lang.Object}
     */
    public static Object eval(String str) {
        if (JSONSerializer.isObject(str)) {
            return JSONSerializer.evalMap(str);
        } else if (JSONSerializer.isArray(str)) {
            return JSONSerializer.evalArray(str);
        } else if (JSONSerializer.isString(str)) {
            return JSONSerializer.evalString(str);
        } else {
            return JSONSerializer.evalInt(str);
        }
    }

    /**
     * 指定したJSON文字列を評価します。
     * 
     * @param str
     *            対象の文字列
     * @return 評価された{@link javac.util.Map}
     */
    public static Map evalMap(String str) {
        Map map = new HashMap();
        StringBuffer buf = new StringBuffer();
        String key = null;
        String value = null;
        boolean valueParse = false;
        boolean isListedData = false;
        boolean isMappedData = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
            case '{':
                if (valueParse) {
                    String sub = str.substring(i, str.length() - 1);
                    int count = 0;
                    for (int j = 0; j < sub.length(); ++j) {
                        char sc = sub.charAt(j);
                        if (sc == '{') {
                            count++;
                            continue;
                        }
                        if (sc == '}' && count > 1) {
                            count--;
                        } else if (sc == '}' && count == 1) {
                            String mapStr = sub.substring(0, j + 1);
                            Map submap = JSONSerializer.evalMap(mapStr);
                            map.put(key, submap);
                            i = i + j;
                            isMappedData = true;
                            break;
                        }
                    }
                }
                break;
            case '}':
                break;
            case '[':
                if (valueParse) {
                    String sub = str.substring(i, str.length() - 1);
                    int count = 0;
                    for (int j = 0; j < sub.length(); ++j) {
                        char sc = sub.charAt(j);
                        if (sc == '[') {
                            count++;
                            continue;
                        }
                        if (sc == ']' && count > 1) {
                            count--;
                        } else if (sc == ']' && count == 1) {
                            String arrayStr = sub.substring(0, j + 1);
                            List list = JSONSerializer.evalArray(arrayStr);
                            map.put(key, list);
                            i = i + j;
                            isListedData = true;
                            break;
                        }
                    }
                }
                break;
            case ']':
                break;
            case ':':
                key = JSONSerializer.evalString(buf.toString().trim());
                valueParse = true;
                buf = new StringBuffer();
                isListedData = false;
                isMappedData = false;
                break;
            case ',':
                if (!isListedData && !isMappedData) {
                    value = buf.toString().trim();
                    valueParse = false;
                    buf = new StringBuffer();
                    evalAndAddMap(key, value, map);
                }
                isListedData = false;
                isMappedData = false;
                break;
            default:
                buf.append(c);
                break;
            }
        }
        if (buf.length() > 0) {
            value = buf.toString().trim();
        }
        if (value != null) {
            evalAndAddMap(key, value, map);
        }
        return map;
    }

    /**
     * 指定したJSON文字列を評価します。
     * 
     * @param str
     *            対象の文字列
     * @return 評価された{@link javac.util.List}
     */
    public static List evalArray(String str) {
        List list = new ArrayList();
        StringBuffer buf = new StringBuffer();
        String value = null;
        boolean isMappedData = false;
        for (int i = 1; i < str.length() - 1; i++) {
            char c = str.charAt(i);
            switch (c) {
            case '{':
                String sub = str.substring(i, str.length() - 1);
                int count = 0;
                for (int j = 0; j < sub.length(); ++j) {
                    char sc = sub.charAt(j);
                    if (sc == '{') {
                        count++;
                        continue;
                    }
                    if (sc == '}' && count > 1) {
                        count--;
                    } else if (sc == '}' && count == 1) {
                        String mapStr = sub.substring(0, j + 1);
                        Map map = JSONSerializer.evalMap(mapStr);
                        list.add(map);
                        i = i + j;
                        isMappedData = true;
                        break;
                    }
                }
                break;
            case '}':
                break;
            case '[':
                buf.append(c);
                break;
            case ']':
                buf.append(c);
                break;
            case ':':
                break;
            case ',':
                if (!isMappedData) {
                    value = buf.toString().trim();
                    buf = new StringBuffer();
                    JSONSerializer.evalAndAddList(value, list);
                }
                isMappedData = false;
                break;
            default:
                buf.append(c);
                break;
            }
        }
        if (buf.length() > 0) {
            value = buf.toString().trim();
        }
        if (value != null) {
            JSONSerializer.evalAndAddList(value, list);
        }
        return list;
    }

    private static void evalAndAddList(String value, List list) {
        if (JSONSerializer.isObject(value)) {
            list.add(JSONSerializer.evalMap(value));
        } else if (JSONSerializer.isArray(value)) {
            list.add(JSONSerializer.evalArray(value));
        } else {
            Integer intValue = evalInt(value);
            if (intValue != null) {
                list.add(intValue);
            } else {
                list.add(JSONSerializer.evalString(value));
            }
        }
    }

    private static void evalAndAddMap(String key, String value, Map map) {
        if (JSONSerializer.isObject(value)) {
            Map v = JSONSerializer.evalMap(value);
            map.put(key, v);
        } else if (JSONSerializer.isArray(value)) {
            List list = JSONSerializer.evalArray(value);
            map.put(key, list);
        } else {
            Integer intValue = evalInt(value);
            if (intValue != null) {
                map.put(key, intValue);
            } else {
                map.put(key, JSONSerializer.evalString(value));
            }
        }
    }

    /**
     * 指定したJSON文字列を評価します。
     * 
     * @param str
     *            対象の文字列
     * @return 評価された{@link javac.lang.String}
     */
    public static String evalString(String str) {
        if (JSONSerializer.isString(str)) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * 指定したJSON文字列を評価します。
     * 
     * @param str
     *            対象の文字列
     * @return 評価された{@link javac.lang.Integer}
     */
    public static Integer evalInt(String str) {
        try {
            int i = Integer.parseInt(str);
            return new Integer(i);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
