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
package org.seasar.extension.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.BigDecimalConversionUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.LruHashMap;
import org.seasar.framework.util.StringUtil;

/**
 * オブジェクトをシリアライズするクラスです。
 * 
 * @author higa
 * 
 */
public abstract class Serializer {

    private static final byte NULL_TYPE = 0;

    private static final byte STRING_TYPE = 1;

    private static final byte INTEGER_TYPE = 2;

    private static final byte BOOLEAN_TYPE = 3;

    private static final byte DATE_TYPE = 4;

    private static final byte BIGDECIMAL_TYPE = 5;

    private static final byte LONG_TYPE = 6;

    private static final byte SHORT_TYPE = 7;

    private static final byte BYTE_TYPE = 8;

    private static final byte FLOAT_TYPE = 9;

    private static final byte DOUBLE_TYPE = 10;

    private static final byte BIGINTEGER_TYPE = 11;

    private static final byte CHARACTER_TYPE = 12;

    private static final byte CALENDAR_TYPE = 13;

    private static final byte SQLDATE_TYPE = 14;

    private static final byte TIME_TYPE = 15;

    private static final byte TIMESTAMP_TYPE = 16;

    private static final byte ARRAY_TYPE = 17;

    private static final byte ARRAYLIST_TYPE = 18;

    private static final byte SHARED_TYPE = 19;

    private static final byte LIST_TYPE = 20;

    private static final byte HASHMAP_TYPE = 21;

    private static final byte LRUHASHMAP_TYPE = 22;

    private static final byte ARRAYMAP_TYPE = 23;

    private static final byte MAP_TYPE = 24;

    private static final byte HASHSET_TYPE = 25;

    private static final byte SET_TYPE = 26;

    private static final byte BEAN_TYPE = 27;

    protected Serializer() {
    }

    /**
     * オブジェクトをDataOutputStreamに書き出します。
     * 
     * @param out
     * @param obj
     */
    public static void writeObject(DataOutputStream out, Object obj) {
        try {
            List sharedObjects = new ArrayList();
            writeObjectInternal(out, obj, sharedObjects);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    protected static void writeObjectInternal(DataOutputStream out, Object obj,
            List sharedObjects) throws IOException {
        if (obj == null) {
            writeNull(out);
        } else {
            Class clazz = obj.getClass();
            if (clazz == String.class) {
                writeString(out, (String) obj);
            } else if (clazz == Integer.class) {
                writeInteger(out, (Integer) obj);
            } else if (clazz == Boolean.class) {
                writeBoolean(out, (Boolean) obj);
            } else if (clazz == Date.class) {
                writeDate(out, (Date) obj);
            } else if (clazz == BigDecimal.class) {
                writeBigDecimal(out, (BigDecimal) obj);
            } else if (clazz == Long.class) {
                writeLong(out, (Long) obj);
            } else if (clazz == Short.class) {
                writeShort(out, (Short) obj);
            } else if (clazz == Byte.class) {
                writeByte(out, (Byte) obj);
            } else if (clazz == Float.class) {
                writeFloat(out, (Float) obj);
            } else if (clazz == Double.class) {
                writeDouble(out, (Double) obj);
            } else if (clazz == BigInteger.class) {
                writeBigInteger(out, (BigInteger) obj);
            } else if (clazz == Character.class) {
                writeCharacter(out, (Character) obj);
            } else if (obj instanceof Calendar) {
                writeCalendar(out, (Calendar) obj);
            } else if (clazz == java.sql.Date.class) {
                writeSqlDate(out, (java.sql.Date) obj);
            } else if (clazz == Time.class) {
                writeTime(out, (Time) obj);
            } else if (clazz == Timestamp.class) {
                writeTimestamp(out, (Timestamp) obj);
            } else if (clazz.isArray()) {
                writeArray(out, obj, sharedObjects);
            } else if (clazz == ArrayList.class) {
                writeArrayList(out, (ArrayList) obj, sharedObjects);
            } else if (obj instanceof List) {
                writeList(out, (List) obj, sharedObjects);
            } else if (clazz == HashMap.class) {
                writeHashMap(out, (HashMap) obj, sharedObjects);
            } else if (clazz == LruHashMap.class) {
                writeLruHashMap(out, (LruHashMap) obj, sharedObjects);
            } else if (clazz == ArrayMap.class) {
                writeArrayMap(out, (ArrayMap) obj, sharedObjects);
            } else if (obj instanceof Map) {
                writeMap(out, (Map) obj, sharedObjects);
            } else if (clazz == HashSet.class) {
                writeHashSet(out, (HashSet) obj, sharedObjects);
            } else if (obj instanceof Set) {
                writeSet(out, (Set) obj, sharedObjects);
            } else {
                writeBean(out, obj, sharedObjects);
            }
        }
    }

    protected static void writeNull(DataOutputStream out) throws IOException {
        out.writeByte(NULL_TYPE);
    }

    protected static void writeString(DataOutputStream out, String s)
            throws IOException {
        out.writeByte(STRING_TYPE);
        out.writeUTF(s);
    }

    protected static void writeInteger(DataOutputStream out, Integer i)
            throws IOException {
        out.writeByte(INTEGER_TYPE);
        out.writeInt(i.intValue());
    }

    protected static void writeBoolean(DataOutputStream out, Boolean b)
            throws IOException {
        out.writeByte(BOOLEAN_TYPE);
        out.writeBoolean(b.booleanValue());
    }

    protected static void writeDate(DataOutputStream out, Date d)
            throws IOException {
        out.writeByte(DATE_TYPE);
        out.writeLong(d.getTime());
    }

    protected static void writeBigDecimal(DataOutputStream out, BigDecimal b)
            throws IOException {
        out.writeByte(BIGDECIMAL_TYPE);
        out.writeUTF(BigDecimalConversionUtil.toString(b));
    }

    protected static void writeLong(DataOutputStream out, Long l)
            throws IOException {
        out.writeByte(LONG_TYPE);
        out.writeLong(l.longValue());
    }

    protected static void writeShort(DataOutputStream out, Short s)
            throws IOException {
        out.writeByte(SHORT_TYPE);
        out.writeShort(s.shortValue());
    }

    protected static void writeByte(DataOutputStream out, Byte b)
            throws IOException {
        out.writeByte(BYTE_TYPE);
        out.writeByte(b.byteValue());
    }

    protected static void writeFloat(DataOutputStream out, Float f)
            throws IOException {
        out.writeByte(FLOAT_TYPE);
        out.writeFloat(f.floatValue());
    }

    protected static void writeDouble(DataOutputStream out, Double d)
            throws IOException {
        out.writeByte(DOUBLE_TYPE);
        out.writeDouble(d.doubleValue());
    }

    protected static void writeBigInteger(DataOutputStream out, BigInteger b)
            throws IOException {
        out.writeByte(BIGINTEGER_TYPE);
        out.writeUTF(b.toString());
    }

    protected static void writeCharacter(DataOutputStream out, Character c)
            throws IOException {
        out.writeByte(CHARACTER_TYPE);
        out.writeChar(c.charValue());
    }

    protected static void writeCalendar(DataOutputStream out, Calendar c)
            throws IOException {
        out.writeByte(CALENDAR_TYPE);
        out.writeLong(c.getTime().getTime());
    }

    protected static void writeSqlDate(DataOutputStream out, java.sql.Date d)
            throws IOException {
        out.writeByte(SQLDATE_TYPE);
        out.writeLong(d.getTime());
    }

    protected static void writeTime(DataOutputStream out, Time t)
            throws IOException {
        out.writeByte(TIME_TYPE);
        out.writeLong(t.getTime());
    }

    protected static void writeTimestamp(DataOutputStream out, Timestamp t)
            throws IOException {
        out.writeByte(TIMESTAMP_TYPE);
        out.writeLong(t.getTime());
    }

    protected static void writeArray(DataOutputStream out, Object array,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, array, sharedObjects)) {
            return;
        }
        out.writeByte(ARRAY_TYPE);
        Class clazz = array.getClass().getComponentType();
        out.writeUTF(clazz.getName());
        int size = Array.getLength(array);
        out.writeInt(size);
        for (int i = 0; i < size; ++i) {
            Object o = Array.get(array, i);
            writeObjectInternal(out, o, sharedObjects);
        }
    }

    protected static boolean writeSharedObject(DataOutputStream out,
            Object obj, List sharedObjects) throws IOException {
        int index = sharedObjects.indexOf(obj);
        if (index < 0) {
            sharedObjects.add(obj);
            return false;
        }
        out.writeByte(SHARED_TYPE);
        out.writeInt(index);
        return true;
    }

    protected static void writeArrayList(DataOutputStream out,
            ArrayList arrayList, List sharedObjects) throws IOException {
        if (writeSharedObject(out, arrayList, sharedObjects)) {
            return;
        }
        out.writeByte(ARRAYLIST_TYPE);
        int size = arrayList.size();
        out.writeInt(size);
        for (int i = 0; i < size; ++i) {
            writeObjectInternal(out, arrayList.get(i), sharedObjects);
        }
    }

    protected static void writeList(DataOutputStream out, List list,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, list, sharedObjects)) {
            return;
        }
        out.writeByte(LIST_TYPE);
        out.writeUTF(list.getClass().getName());
        int size = list.size();
        out.writeInt(size);
        for (Iterator i = list.iterator(); i.hasNext();) {
            writeObjectInternal(out, i.next(), sharedObjects);
        }
    }

    protected static void writeHashMap(DataOutputStream out, HashMap obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(HASHMAP_TYPE);
        int size = obj.size();
        out.writeInt(size);
        for (Iterator i = obj.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            writeObjectInternal(out, e.getKey(), sharedObjects);
            writeObjectInternal(out, e.getValue(), sharedObjects);
        }
    }

    protected static void writeArrayMap(DataOutputStream out, ArrayMap obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(ARRAYMAP_TYPE);
        int size = obj.size();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            Map.Entry e = obj.getEntry(i);
            writeObjectInternal(out, e.getKey(), sharedObjects);
            writeObjectInternal(out, e.getValue(), sharedObjects);
        }
    }

    protected static void writeLruHashMap(DataOutputStream out, LruHashMap obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(LRUHASHMAP_TYPE);
        int size = obj.size();
        out.writeInt(size);
        int limitSize = obj.getLimitSize();
        out.writeInt(limitSize);
        for (Iterator i = obj.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            writeObjectInternal(out, e.getKey(), sharedObjects);
            writeObjectInternal(out, e.getValue(), sharedObjects);
        }
    }

    protected static void writeMap(DataOutputStream out, Map obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(MAP_TYPE);
        out.writeUTF(obj.getClass().getName());
        int size = obj.size();
        out.writeInt(size);
        for (Iterator i = obj.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            writeObjectInternal(out, e.getKey(), sharedObjects);
            writeObjectInternal(out, e.getValue(), sharedObjects);
        }
    }

    protected static void writeHashSet(DataOutputStream out, HashSet obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(HASHSET_TYPE);
        int size = obj.size();
        out.writeInt(size);
        for (Iterator i = obj.iterator(); i.hasNext();) {
            writeObjectInternal(out, i.next(), sharedObjects);
        }
    }

    protected static void writeSet(DataOutputStream out, Set obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(SET_TYPE);
        out.writeUTF(obj.getClass().getName());
        int size = obj.size();
        out.writeInt(size);
        for (Iterator i = obj.iterator(); i.hasNext();) {
            writeObjectInternal(out, i.next(), sharedObjects);
        }
    }

    protected static void writeBean(DataOutputStream out, Object obj,
            List sharedObjects) throws IOException {
        if (writeSharedObject(out, obj, sharedObjects)) {
            return;
        }
        out.writeByte(BEAN_TYPE);
        Class clazz = obj.getClass();
        out.writeUTF(clazz.getName());
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
        int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (pd.hasReadMethod() && pd.hasWriteMethod()) {
                out.writeUTF(pd.getPropertyName());
                Object value = pd.getValue(obj);
                writeObjectInternal(out, value, sharedObjects);
            }
        }
        out.writeUTF("");
    }

    /**
     * DataInputStreamからオブジェクトを読み取ります。
     * 
     * @param in
     * @return
     */
    public static Object readObject(DataInputStream in) {
        try {
            List sharedObjects = new ArrayList();
            return readObjectInternal(in, sharedObjects);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    protected static Object readObjectInternal(DataInputStream in,
            List sharedObjects) throws IOException {
        byte type = in.readByte();
        switch (type) {
        case NULL_TYPE:
            return null;
        case STRING_TYPE:
            return readString(in);
        case INTEGER_TYPE:
            return readInteger(in);
        case BOOLEAN_TYPE:
            return readBoolean(in);
        case DATE_TYPE:
            return readDate(in);
        case BIGDECIMAL_TYPE:
            return readBigDecimal(in);
        case LONG_TYPE:
            return readLong(in);
        case SHORT_TYPE:
            return readShort(in);
        case BYTE_TYPE:
            return readByte(in);
        case FLOAT_TYPE:
            return readFloat(in);
        case DOUBLE_TYPE:
            return readDouble(in);
        case BIGINTEGER_TYPE:
            return readBigInteger(in);
        case CHARACTER_TYPE:
            return readCharacter(in);
        case CALENDAR_TYPE:
            return readCalendar(in);
        case SQLDATE_TYPE:
            return readSqlDate(in);
        case TIME_TYPE:
            return readTime(in);
        case TIMESTAMP_TYPE:
            return readTimestamp(in);
        case ARRAY_TYPE:
            return readArray(in, sharedObjects);
        case ARRAYLIST_TYPE:
            return readArrayList(in, sharedObjects);
        case SHARED_TYPE:
            return readSharedObject(in, sharedObjects);
        case LIST_TYPE:
            return readList(in, sharedObjects);
        case HASHMAP_TYPE:
            return readHashMap(in, sharedObjects);
        case LRUHASHMAP_TYPE:
            return readLruHashMap(in, sharedObjects);
        case ARRAYMAP_TYPE:
            return readArrayMap(in, sharedObjects);
        case MAP_TYPE:
            return readMap(in, sharedObjects);
        case HASHSET_TYPE:
            return readHashSet(in, sharedObjects);
        case SET_TYPE:
            return readSet(in, sharedObjects);
        case BEAN_TYPE:
            return readBean(in, sharedObjects);
        }
        throw new IllegalStateException("unkowon type:" + type);
    }

    protected static Object readString(DataInputStream in) throws IOException {
        return in.readUTF();
    }

    protected static Object readInteger(DataInputStream in) throws IOException {
        return new Integer(in.readInt());
    }

    protected static Object readBoolean(DataInputStream in) throws IOException {
        return Boolean.valueOf(in.readBoolean());
    }

    protected static Object readDate(DataInputStream in) throws IOException {
        return new Date(in.readLong());
    }

    protected static Object readBigDecimal(DataInputStream in)
            throws IOException {
        return new BigDecimal(in.readUTF());
    }

    protected static Object readLong(DataInputStream in) throws IOException {
        return Long.valueOf(in.readLong());
    }

    protected static Object readShort(DataInputStream in) throws IOException {
        return Short.valueOf(in.readShort());
    }

    protected static Object readByte(DataInputStream in) throws IOException {
        return Byte.valueOf(in.readByte());
    }

    protected static Object readFloat(DataInputStream in) throws IOException {
        return Float.valueOf(in.readFloat());
    }

    protected static Object readDouble(DataInputStream in) throws IOException {
        return Double.valueOf(in.readDouble());
    }

    protected static Object readBigInteger(DataInputStream in)
            throws IOException {
        return new BigInteger(in.readUTF());
    }

    protected static Object readCharacter(DataInputStream in)
            throws IOException {
        return Character.valueOf(in.readChar());
    }

    protected static Object readCalendar(DataInputStream in) throws IOException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(in.readLong()));
        return c;
    }

    protected static Object readSqlDate(DataInputStream in) throws IOException {
        return new java.sql.Date(in.readLong());
    }

    protected static Object readTime(DataInputStream in) throws IOException {
        return new Time(in.readLong());
    }

    protected static Object readTimestamp(DataInputStream in)
            throws IOException {
        return new Timestamp(in.readLong());
    }

    protected static Object readArray(DataInputStream in, List sharedObjects)
            throws IOException {
        String typeName = in.readUTF();
        Class type = ClassUtil.convertClass(typeName);
        int size = in.readInt();
        Object array = Array.newInstance(type, size);
        sharedObjects.add(array);
        for (int i = 0; i < size; ++i) {
            Array.set(array, i, readObjectInternal(in, sharedObjects));
        }
        return array;
    }

    protected static Object readArrayList(DataInputStream in, List sharedObjects)
            throws IOException {
        int size = in.readInt();
        ArrayList arrayList = new ArrayList(size);
        sharedObjects.add(arrayList);
        for (int i = 0; i < size; ++i) {
            arrayList.add(readObjectInternal(in, sharedObjects));
        }
        return arrayList;
    }

    protected static Object readSharedObject(DataInputStream in,
            List sharedObjects) throws IOException {
        int index = in.readInt();
        return sharedObjects.get(index);
    }

    protected static Object readList(DataInputStream in, List sharedObjects)
            throws IOException {
        String typeName = in.readUTF();
        List list = (List) ClassUtil.newInstance(typeName);
        sharedObjects.add(list);
        int size = in.readInt();
        for (int i = 0; i < size; ++i) {
            list.add(readObjectInternal(in, sharedObjects));
        }
        return list;
    }

    protected static Object readHashMap(DataInputStream in, List sharedObjects)
            throws IOException {
        int size = in.readInt();
        HashMap map = new HashMap(size);
        sharedObjects.add(map);
        for (int i = 0; i < size; ++i) {
            Object key = readObjectInternal(in, sharedObjects);
            Object value = readObjectInternal(in, sharedObjects);
            map.put(key, value);
        }
        return map;
    }

    protected static Object readLruHashMap(DataInputStream in,
            List sharedObjects) throws IOException {
        int size = in.readInt();
        int limitSize = in.readInt();
        LruHashMap map = new LruHashMap(limitSize);
        sharedObjects.add(map);
        for (int i = 0; i < size; ++i) {
            Object key = readObjectInternal(in, sharedObjects);
            Object value = readObjectInternal(in, sharedObjects);
            map.put(key, value);
        }
        return map;
    }

    protected static Object readArrayMap(DataInputStream in, List sharedObjects)
            throws IOException {
        int size = in.readInt();
        ArrayMap map = new ArrayMap(size);
        sharedObjects.add(map);
        for (int i = 0; i < size; ++i) {
            Object key = readObjectInternal(in, sharedObjects);
            Object value = readObjectInternal(in, sharedObjects);
            map.put(key, value);
        }
        return map;
    }

    protected static Object readMap(DataInputStream in, List sharedObjects)
            throws IOException {
        String typeName = in.readUTF();
        Map map = (Map) ClassUtil.newInstance(typeName);
        sharedObjects.add(map);
        int size = in.readInt();
        for (int i = 0; i < size; ++i) {
            Object key = readObjectInternal(in, sharedObjects);
            Object value = readObjectInternal(in, sharedObjects);
            map.put(key, value);
        }
        return map;
    }

    protected static Object readHashSet(DataInputStream in, List sharedObjects)
            throws IOException {
        int size = in.readInt();
        HashSet set = new HashSet(size);
        sharedObjects.add(set);
        for (int i = 0; i < size; ++i) {
            set.add(readObjectInternal(in, sharedObjects));
        }
        return set;
    }

    protected static Object readSet(DataInputStream in, List sharedObjects)
            throws IOException {
        String typeName = in.readUTF();
        Set set = (Set) ClassUtil.newInstance(typeName);
        sharedObjects.add(set);
        int size = in.readInt();
        for (int i = 0; i < size; ++i) {
            set.add(readObjectInternal(in, sharedObjects));
        }
        return set;
    }

    protected static Object readBean(DataInputStream in, List sharedObjects)
            throws IOException {
        String typeName = in.readUTF();
        Class type = ClassUtil.forName(typeName);
        Object bean = ClassUtil.newInstance(type);
        sharedObjects.add(bean);
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(type);
        while (true) {
            String propertyName = in.readUTF();
            if (StringUtil.isEmpty(propertyName)) {
                break;
            }
            Object value = readObjectInternal(in, sharedObjects);
            PropertyDesc pd = beanDesc.getPropertyDesc(propertyName);
            if (pd != null && pd.hasWriteMethod()) {
                pd.setValue(bean, value);
            }
        }
        return bean;
    }
}